(ns graalf.utils
  (:require [clojure.string :as str]))

(defn expand-home [path]
  (if (str/starts-with? path "~/")
    (str/replace path #"^~" (System/getProperty "user.home"))
    path))

(defn type-to-letter
  [type]
  (case type
    "cm:folder" "F"
    "st:sites" "S"
    ""))