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
  (println msg)
  (System/exit status))

(defn get-ticket
  [parms]
  (let [response (auth/create-ticket (:user parms) (:password parms))]
    (if (= 201 (:status response))
      (println (get-in response [:body :entry :id]))
      (exit (:status response) (:message response)))))

(defn list-children
  [parms]
  (println (nodes/list-node-children {:id (:ticket parms) :user-id (:user parms)} (:parent-id parms))))

;; cli-matic config
(def CONFIGURATION
  {:app      {:command     c/program-name
              :description c/program-description
              :version     c/program-version}
   :commands [{:command     "ticket"
               :description ["get a ticket"]
               :opts        [{:option "user" :short "u" :default :present :type :string}
                             {:option "password" :short "p" :default :present :type :string}]
               :runs        get-ticket}
              {:command     "children"
               :description ["list children"]
               :opts        [{:option "user" :short "u" :type :string}
                             {:option "ticket" :short "t" :type :string}
                             {:option "parent-id" :short "p" :default :present :type :string}]
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

  (run-cmd args CONFIGURATION))