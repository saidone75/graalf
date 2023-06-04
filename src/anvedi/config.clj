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

(defn- adjust-paths [c]
  (w/postwalk #(if (string? %) (utils/expand-home %) %) c))

(defn- create-with-defaults
  [f]
  (let [cfg-dir (io/file (str (System/getProperty "user.home") "/.anvedi"))]
    (if-not (.exists cfg-dir) (.mkdir cfg-dir)))
  (spit f {:scheme "http"
           :host   "localhost"
           :port   8080}))

(defn load-config [f]
  (if (.exists (io/file f))
    (reset! config
            (adjust-paths (immu/load f)))
    (create-with-defaults f)))