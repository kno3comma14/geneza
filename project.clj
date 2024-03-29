(defproject geneza "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.cli "1.0.214"]
                 [com.datomic/datomic-free "0.9.5697"]
                 [org.clojure/java.classpath "1.0.0"]
                 [selmer "1.12.53"]
                 [cljfmt "0.9.2"]]
  :main ^:skip-aot geneza.core
  :target-path "target/%s"
  :resource-paths ["resources"]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:dependencies [[lambdaisland/kaocha "1.80.1274"]
                                  [lambdaisland/kaocha-cloverage "1.1.89"]]}}
  :aliases {"kaocha" ["run" "-m" "kaocha.runner"]})
