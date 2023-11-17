(ns geneza.test-constants)

(def project-clj-content "(defproject test-project \"v1.0.0\"\n  :description \"Project for testing purposes\"\n  :url \"http://testies.com\"\n  :dependencies [[org.clojure/clojure \"1.10.1\"]\n                 [io.pedestal/pedestal.service \"0.6.1\"]\n                 [io.pedestal/pedestal.jetty \"0.6.1\"]\n                 [ch.qos.logback/logback-classic \"1.2.10\" :exclusions [org.slf4j/slf4j-api]]\n                 [org.slf4j/jul-to-slf4j \"1.7.35\"]\n                 [org.slf4j/jcl-over-slf4j \"1.7.35\"]\n                 [org.slf4j/log4j-over-slf4j \"1.7.35\"]]\n  :min-lein-version \"2.0.0\"\n  :resource-paths [\"config\", \"resources\"]\n  :profiles {:dev {:aliases {\"run-dev\" [\"trampoline\" \"run\" \"-m\" \"test-project.server/run-dev\"]}\n                   :dependencies [[io.pedestal/pedestal.service-tools \"0.6.1\"]]}\n             :uberjar {:aot [.server]}}\n  :main ^{:skip-aot true} test-project.server)")
