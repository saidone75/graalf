(ns anvedi.utils
  (:require [clojure.string :as str]))

(defn expand-home [path]
  (if (str/starts-with? path "~/")
    (str/replace path #"^~" (System/getProperty "user.home"))
    path))