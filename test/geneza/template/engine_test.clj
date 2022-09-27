(ns geneza.template.engine-test
  (:require [clojure.test :refer [deftest testing is]]
            [geneza.test-constants :as constants]
            [geneza.template.engine :as engine]
            [clojure.java.io :as io]))


(deftest parse-template-test
  (testing "Templates are being parsed correctly"
    (let [template-url "kit/project.clj.template"
          template-map {:project-name "test-project"
                        :project-description "Project for testing purposes"
                        :project-version "v1.0.0"
                        :project-url "http://testies.com"}
          actual-value (engine/parse-template template-url template-map)
          expected-value constants/project-clj-content]
      (is (= expected-value expected-value)))))

(deftest build-template-test
  (testing "Templates are being built correctly"
    (let [template-file-url "kit/project.clj.template"
          template-map {:project-name "test-project"
                        :project-description "Project for testing purposes"
                        :project-version "v1.0.0"
                        :project-url "http://testies.com"}
          temporal-folder "temp/"
          template-name "project.clj"
          _ (engine/build-template template-file-url template-map temporal-folder template-map)
          existence-validator (.exists (io/file (str temporal-folder template-name)))
          expected-value true]
      (is (= existence-validator expected-value)))))
