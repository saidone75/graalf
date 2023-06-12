(ns anvedi.core
  (:require
    [anvedi.config :as c]
    [anvedi.utils :as utils]
    [cli-matic.core :refer [run-cmd]]
    [cral.alfresco.auth :as auth]
    [cral.alfresco.config :as config]
    [cral.alfresco.core.nodes :as nodes])
  (:gen-class))

(defn- exit
  [status msg]
  (if-not (nil? msg) (println msg))
  (System/exit status))

(defn authenticate
  [parms]
  (let [response (auth/create-ticket (:user parms) (:password parms))]
    (if (= 201 (:status response))
      (let [ticket (get-in response [:body :entry])]
        ;; save ticket in config atom
        (swap! c/config assoc :ticket ticket)
        ;; write changes to config file
        (c/save-config)
        (exit 0 "Authentication successful."))
      (exit (:status response) (:message response)))))

(defn execute-authenticated
  [f parms]
  (if (and (not (nil? (:ticket @c/config))) (= 200 (:status (auth/validate-ticket (:ticket @c/config)))))
    (do
      (f parms)
      (exit 0 nil))
    (exit 1 "Ticket not present or expired, please authenticate.")))

(defn list-children
  [parms]
  (execute-authenticated #(clojure.pprint/pprint (nodes/list-node-children (:ticket @c/config) (:parent-id %))) parms))

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
               :opts        [{:option "parent-id" :short "i" :default :present :type :string}]
               :runs        list-children}
              ]})

(defn -main
  [& args]
  ;; load configuration
  (try
    (c/load-config (utils/expand-home "~/.anvedi/anvedi.cfg.edn"))
    (catch Exception e (exit 1 (.getMessage e))))
  ;; configure CRAL
  (config/configure @c/config)
  ;; run program
  (run-cmd args CONFIGURATION))