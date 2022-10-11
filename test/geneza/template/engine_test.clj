(ns geneza.template.engine-test
  (:require [clojure.test :refer [deftest testing is]]
            [geneza.test-constants :as constants]
            [geneza.template.engine :as engine]
            [geneza.template.util :as util]
            [clojure.java.io :as io]))

(def project-clj-file "resources/temp/project.clj")
(def project-structure-info {:folder-structure {"" [{:template "project.clj.template"
                                                     :template-path "kit"
                                                     :template-map {:project-name "test-project"
                                                                    :project-description "Project for testing purposes"
                                                                    :project-version "v1.0.0"
                                                                    :project-url "http://testies.com"}
                                                     :file-path "resources/temp/project.clj"}
                                                    {:template "Dockerfile.template"
                                                     :template-path "kit"
                                                     :template-map {:application-name "test-project"}
                                                     :file-path "resources/temp/Dockerfile"}]
                                                "another-folder" [{:template "Makefile.template"
                                                                   :template-path "kit"
                                                                   :template-map {}
                                                                   :file-path "resources/temp/another-folder/Makefile"}]}
                             :context {}}) ;; temporal reference

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
          temporal-folder "resources/temp/project.clj"
          _ (engine/build-template template-file-url template-map temporal-folder)
          existence-validator (.exists (io/file temporal-folder))
          expected-value true]
      (is (= existence-validator expected-value)))))

(deftest build-project-hierarchy-test
  (testing "Project hierarchy built correctly"
    (let [_ (engine/build-project-hierarchy project-structure-info)
          existence-validator false
          expected-value true]
      (is (= existence-validator true)))))


