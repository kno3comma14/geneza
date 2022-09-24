(ns geneza.template.engine-test
  (:require [clojure.test :refer [deftest testing is]]
            [geneza.template.engine :as engine]))

(def template-expected "(defproject test-project \"v1.0.0\"\n  :description \"Project for testing purposes\"\n  :url \"http://testies.com\"\n  :dependencies [\n    [org.clojure/clojure \"1.10.0\"]\n    [org.clojure/data.json \"0.2.6\"]\n    [ring/ring-defaults \"0.3.2\"]\n    [ring/ring-devel \"1.6.3\"]\n    [ring/ring-json \"0.5.0\"]\n    [com.datomic/datomic-free \"0.9.5697\"]\n    [compojure \"1.6.1\"]\n    [http-kit \"2.3.0\"]\n    [lynxeyes/dotenv \"1.0.2\"]]\n  :main ^:skip-aot test-project.core\n  :target-path \"target/%s\"\n  :profiles {:uberjar {:aot :all} :dev {:main test-project.core/-dev-main}})")


(deftest parse-template-test
  (testing "Templates are building properly"
    (let [template-url "kit/project.clj.template"
          template-map {:project-name "test-project"
                        :project-description "Project for testing purposes"
                        :project-version "v1.0.0"
                        :project-url "http://testies.com"}
          actual-value (engine/parse-template template-url template-map)
          expected-value template-expected]
      (is (= expected-value expected-value)))))
