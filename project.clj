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

(defproject graalf "0.1.0-SNAPSHOT"
  :description "A testbed project using CRAL library for interacting with Alfresco. Compatible with GraalVM."
  :url "https://saidone.org"
  :license {:name "GNU General Public License v3.0"
            :url  "https://www.gnu.org/licenses/gpl-3.0.txt"}
  :dependencies [[org.clojure/clojure "1.11.2"]
                 [russellwhitaker/immuconf "0.3.0"]
                 [cli-matic "0.5.4"]
                 [com.github.clj-easy/graal-build-time "1.0.5"]
                 [jp.ne.tir/project-clj "0.1.7"]
                 [org.saidone/cral "0.3.5"]]
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  :main ^:skip-aot graalf.core
  :jvm-opts ["-Dclojure.tools.logging.factory=clojure.tools.logging.impl/jul-factory"]
  :target-path "target/%s"
  :native-image {:name "graalf"
                 :opts ["-J-Xmx3g"
                        "--report-unsupported-elements-at-runtime"
                        "-H:EnableURLProtocols=http"
                        "-H:ReflectionConfigurationFiles=./reflectconfig.json"
                        "--features=clj_easy.graal_build_time.InitClojureClasses"]}
  :profiles {:uberjar
             {:aot      :all
              :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                         "-Dclojure.spec.skip-macros=true"]}})
