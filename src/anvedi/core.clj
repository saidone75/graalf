(ns anvedi.core
  (:require
    [anvedi.config :as c]
    [anvedi.utils :as utils]
    [cral.alfresco.config :as config]
    [cral.alfresco.auth :as auth]
    [cral.alfresco.core.nodes :as nodes]
    [cli-matic.core :refer [run-cmd]])
  (:gen-class))

(defn- exit [status msg]
  (println msg)
  (System/exit status))

(defn get-ticket [parms]
  (let [response (auth/create-ticket (:user parms) (:password parms))]
    (if (= 201 (:status response))
      (println (get-in response [:body :entry :id]))
      (exit (:status response) (:message response)))))

;; cli-matic config
(def CONFIGURATION
  {:app      {:command     c/program-name
              :description c/program-description
              :version     c/program-version}
   :commands [
              {:command     "ticket"
               :description ["get a ticket"]
               :opts        [{:option "user" :short "u" :default :present :type :string}
                             {:option "password" :short "p" :default :present :type :string}]
               :runs        get-ticket}]})

(defn -main
  [& args]
  ;; load configuration
  (try
    (c/load-config (utils/expand-home "~/.anvedi/anvedi.cfg.edn"))
    (catch Exception e (exit 1 (.getMessage e))))
  ;; configure CRAL
  (config/configure @c/config)

  (run-cmd args CONFIGURATION))