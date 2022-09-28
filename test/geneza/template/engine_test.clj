(ns geneza.template.engine-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [geneza.test-constants :as constants]
            [geneza.template.engine :as engine]
            [geneza.template.util :as util]
            [clojure.java.io :as io]))

(def project-clj-file "resources/temp/project.clj")

;; Fixtures Definition
(defn teardown
  [file-to-delete]
  (when (.exists (io/file file-to-delete))
    (util/delete-aux-file file-to-delete)))

(defn engine-test-fixture
  [f]
  f
  (teardown project-clj-file))

(use-fixtures :once engine-test-fixture)

(deftest parse-template-test
  (testing "Templates are being parsed correctly"
    (let [template-url "kit/project.clj.template"
          template-map {:project-name "test-project"
                        :project-description "Project for testing purposes"
                        :project-version "v1.0.0"
                        :project-url "http://testies.com"}
          actual-value (engine/parse-template template-url template-map)
          expected-value constants/project-clj-content]
      (is (= expected-value actual-value)))))

(deftest build-template-test
  (testing "Templates are being built correctly"
    (let [template-file-url "kit/project.clj.template"
          template-map {:project-name "test-project"
                        :project-description "Project for testing purposes"
                        :project-version "v1.0.0"
                        :project-url "http://testies.com"}
          temporal-folder "resources/temp/"
          template-name "project.clj"
          _ (engine/build-template template-file-url template-map temporal-folder template-name)
          existence-validator (.exists (io/file (str temporal-folder template-name)))
          expected-value true]
      (is (= existence-validator expected-value)))))
