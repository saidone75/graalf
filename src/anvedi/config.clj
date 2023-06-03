(ns anvedi.config
  (:require [clojure.string :as str]
            [clojure.walk :as w]
            [immuconf.config :as immu]
            [project-clj.core :as project-clj]
            [anvedi.utils :as utils]))

(def config (atom {}))

;; from project.clj
(def program-name (project-clj/get :name))
(def program-version (project-clj/get :version))
(def program-description (project-clj/get :description))

(defn- adjust-paths [c]
  (w/postwalk #(if (string? %) (utils/expand-home %) %) c))

(defn load-config [f]
  (reset! config
          (adjust-paths (immu/load f))))
