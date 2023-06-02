(ns anvedi.core
  (:require
    [cral.alfresco.config :as config]
    [cral.alfresco.auth :as auth])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (auth/create-ticket "admin" "admin")))