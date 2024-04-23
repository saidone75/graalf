(ns graalf.core
  (:require
    [graalf.config :as c]
    [graalf.utils :as utils]
    [cli-matic.core :refer [run-cmd]]
    [cral.api.auth :as auth]
    [cral.config :as config]
    [cral.api.core.nodes :as nodes])
  (:gen-class))

(defn- exit
  [status msg]
  (if-not (nil? msg) (println msg))
  (System/exit status))

(defn- authenticate
  [params]
  (let [response (auth/create-ticket (:user params) (:password params))]
    (if (= 201 (:status response))
      (let [ticket (get-in response [:body :entry])]
        ;; save ticket in config atom
        (swap! c/config assoc :ticket ticket)
        ;; write changes to config file
        (c/save-config)
        (exit 0 "Authentication successful."))
      (exit (:status response) (:message response)))))

(defn- execute-authenticated
  "Execute f with params only if a valid ticket is present on config atom."
  [f params]
  (if (and (not (nil? (:ticket @c/config))) (= 200 (:status (auth/validate-ticket (:ticket @c/config)))))
    (do
      (f params)
      (exit 0 nil))
    (exit 1 "Ticket not present or expired, please (re)authenticate.")))

(defn- list-children
  [params]
  (execute-authenticated
    (fn
      [params]
      (if (:json params)
        (clojure.pprint/pprint (nodes/list-node-children (:ticket @c/config) (:parent-id params)))
        (run!
          #(println (format "%1s %36s %s" (utils/type-to-letter (:node-type %)) (:id %) (:name %)))
          (map :entry (get-in (nodes/list-node-children (:ticket @c/config) (:parent-id params)) [:body :list :entries])))))
    params))

;; cli-matic config
(def CONFIGURATION
  {:app      {:command     c/program-name
              :description c/program-description
              :version     c/program-version}
   :commands [{:command     "auth"
               :description ["authenticate"]
               :opts        [{:option "user" :short "u" :default :present :type :string}
                             {:option "password" :short "p" :default :present :type :string}]
               :runs        authenticate}
              {:command     "children"
               :description ["list children"]
               :opts        [{:option "parent-id" :short "p" :default :present :type :string}
                             {:option "json" :short "j" :type :with-flag :default false}]
               :runs        list-children}
              ]})

(defn -main
  [& args]
  ;; load configuration
  (try
    (c/load-config (utils/expand-home "~/.graalf/graalf.cfg.edn"))
    (catch Exception e (exit 1 (.getMessage e))))
  ;; configure CRAL
  (config/configure @c/config)
  ;; run program
  (run-cmd args CONFIGURATION))