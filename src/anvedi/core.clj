(ns anvedi.core
  (:require
    [anvedi.config :as c]
    [anvedi.utils :as utils]
    [cral.alfresco.config :as config]
    [cral.alfresco.auth :as auth]
    [cral.alfresco.core.nodes :as nodes])
  (:gen-class))

(defn- exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  [& args]
  ;; load configuration
  (try
    (c/load-config (utils/expand-home "~/.anvedi/anvedi.cfg.edn"))
    (catch Exception e (exit 1 (.getMessage e))))
  ;; configure CRAL
  (config/configure @c/config)
  ;; ask for a ticket and retrieve -root- node
  (let [ticket (get-in (auth/create-ticket "admin" "admin") [:body :entry])]
    (nodes/get-node ticket "-root-")))