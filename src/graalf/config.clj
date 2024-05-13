;  graalf
;  Copyright (C) 2023-2024 Saidone
;
;  This program is free software: you can redistribute it and/or modify
;  it under the terms of the GNU General Public License as published by
;  the Free Software Foundation, either version 3 of the License, or
;  (at your option) any later version.
;
;  This program is distributed in the hope that it will be useful,
;  but WITHOUT ANY WARRANTY; without even the implied warranty of
;  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;  GNU General Public License for more details.
;
;  You should have received a copy of the GNU General Public License
;  along with this program.  If not, see <http://www.gnu.org/licenses/>.

(ns graalf.config
  (:require [clojure.java.io :as io]
            [clojure.walk :as w]
            [graalf.utils :as utils]
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
  (let [cfg-dir (io/file (str (System/getProperty "user.home") "/.graalf"))]
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
    (io/file (utils/expand-home "~/.graalf/graalf.cfg.edn"))
    @config))