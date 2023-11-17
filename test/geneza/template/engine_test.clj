(ns geneza.template.engine-test
  (:require [clojure.test :refer [deftest testing is]]
            [geneza.test-constants :as constants]
            [geneza.template.engine :as engine]
            [clojure.java.io :as io]))

(def project-clj-file "resources/temp/project.clj")
(def project-structure-info {:folder-structure {"" [{:template "project.clj.template"
                                                     :template-path "clojure/pedestal"
                                                     :template-map {:project-name "test-project"
                                                                    :project-description "Project for testing purposes"
                                                                    :project-version "v1.0.0"
                                                                    :project-url "http://testies.com"}
                                                     :file-path "resources/temp/project2.clj"}
                                                    {:template "Dockerfile.template"
                                                     :template-path "clojure/pedestal"
                                                     :template-map {:application-name "test-project"}
                                                     :file-path "resources/temp/Dockerfile"}]
                                                "another-folder" [{:template "Makefile.template"
                                                                   :template-path "clojure/pedestal"
                                                                   :template-map {}
                                                                   :file-path "resources/temp/another-folder/Makefile"}]}
                             :context {}}) ;; temporal reference

(deftest parse-template-test
  (testing "Templates are being parsed correctly"
    (let [template-url "clojure/pedestal/project.clj.template"
          template-map {:project-name "test-project"
                        :project-description "Project for testing purposes"
                        :project-version "v1.0.0"
                        :project-url "http://testies.com"}
          actual-value (engine/parse-template template-url template-map)
          expected-value constants/project-clj-content]
      (is (= expected-value actual-value)))))

(deftest build-template-test
  (testing "Templates are being built correctly"
    (let [template-file-url "clojure/pedestal/project.clj.template"
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
          dockerfile-existence-validator (.exists (io/file "resources/temp/Dockerfile"))
          makefile-existence-validator (.exists (io/file "resources/temp/another-folder/Makefile"))
          project-clj-existence-validator (.exists (io/file "resources/temp/project2.clj"))
          actual-existence-value (and dockerfile-existence-validator makefile-existence-validator project-clj-existence-validator)
          expected-existence-value true]
      (is (= expected-existence-value actual-existence-value)))))


