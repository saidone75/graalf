(defproject anvedi "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [cli-matic "0.5.4"]
                 [com.github.clj-easy/graal-build-time "0.1.4"]
                 [cral "0.1.0-SNAPSHOT"]]
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  :main ^:skip-aot anvedi.core
  :jvm-opts ["-Dclojure.tools.logging.factory=clojure.tools.logging.impl/jul-factory"]
  :target-path "target/%s"
  :native-image {:name "anvedi"
                 :opts ["--no-server"
                        "-J-Xmx3g"
                        "--report-unsupported-elements-at-runtime"
                        "-H:EnableURLProtocols=http"
                        "-H:ReflectionConfigurationFiles=./reflectconfig.json"]}
  :profiles {:uberjar
             {:aot :all
              :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                         "-Dclojure.spec.skip-macros=true"]}})
