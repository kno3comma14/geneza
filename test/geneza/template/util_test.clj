(ns geneza.template.util-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [clojure.java.io :as io]
            [geneza.template.util :as util]))

(def test-file-path (str (System/getProperty "user.dir") "/resources/aux-folder"))

;; Fixtures definition

(defn initial-folder-deletion
  [path]
  (when (.exists (io/file path))
    (io/delete-file path)))

(defn create-test-folders []
  ;; TODO
  nil)

(defn create-aux-folder-test-fixture
  [f]
  (initial-folder-deletion test-file-path)
  (f)
  (initial-folder-deletion test-file-path))

(use-fixtures :once create-aux-folder-test-fixture)

(deftest get-file-directory-test
  (testing "The correct cannocial path is returned"
    (let [input-path "./resources"
          actual-value (util/get-file-directory input-path)
          root-folder (System/getProperty "user.dir")
          expected-value (str root-folder "/" "resources")]
      (is (= expected-value actual-value)))))

(deftest create-aux-folder-test
  (testing "The folder have created succesfully"
    (util/create-aux-folder test-file-path)
    (let [file-exists? (.exists (io/file test-file-path))]
      (is  file-exists?))))

(deftest delete-aux-folder
  (testing "The aux-folder is succesfully deleted"))
