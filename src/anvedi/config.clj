(ns anvedi.config
  (:require [anvedi.utils :as utils]
            [clojure.java.io :as io]
            [clojure.walk :as w]
            [immuconf.config :as immu]
            [project-clj.core :as project-clj]))

(def config (atom {}))

;; from project.clj
(def program-name (project-clj/get :name))
(def program-version (project-clj/get :version))
(def program-description (project-clj/get :description))

(defn- adjust-paths
  "Expand ~ with full path."
  [c]
  (w/postwalk #(if (string? %) (utils/expand-home %) %) c))

(defn- create-with-defaults
  "Create a new config file with default values."
  [f]
  (let [cfg-dir (io/file (str (System/getProperty "user.home") "/.anvedi"))]
    (if-not (.exists cfg-dir) (.mkdir cfg-dir)))
  (spit f {:scheme "http"
           :host   "localhost"
           :port   8080}))

(defn load-config
  "Load config file and overwrite config atom."
  [f]
  (if (.exists (io/file f))
    (reset! config
            (adjust-paths (immu/load f)))
    (create-with-defaults f)))

(defn save-config
  "Save config file with the current content of config atom."
  []
  (spit
    (io/file (utils/expand-home "~/.anvedi/anvedi.cfg.edn"))
    @config))