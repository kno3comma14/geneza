(ns geneza.template.util-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [clojure.java.io :as io]
            [geneza.template.util :as util]))

(def test-file-path (str (System/getProperty "user.dir") "/resources/aux-folder"))
(def base-folder (str (System/getProperty "user.dir") "/resources"))
(def test-paths [(java.io.File. (str base-folder "/test-folder1"))
                 (java.io.File. (str base-folder "/test-folder1/inner-1"))
                 (java.io.File. (str base-folder "/test-folder1/inner-1/inner-2"))
                 (java.io.File. (str base-folder "/test-folder1/base-level"))])

(defn create-test-folders
  [path-list]
  (doseq [path path-list]
    (.mkdir path)))

(defn delete-test-folders [path-list]
  (util/delete-aux-folder (nth path-list 0)))

(defn initial-folder-deletion
  [path]
  (when (.exists (io/file path))
    (io/delete-file path)))

(defn setup
  [single-path hierarchi-path]
  (initial-folder-deletion single-path)
  (create-test-folders hierarchi-path))

(defn teardown
  [single-path hierarchi-path]
  (initial-folder-deletion single-path)
  (when (.exists (nth hierarchi-path 0))
    (delete-test-folders hierarchi-path)))

;; Fixtures definition

(defn create-aux-folder-test-fixture
  [f]
  (setup test-file-path test-paths)
  (f)
  (teardown test-file-path test-paths))

(use-fixtures :once create-aux-folder-test-fixture)

(deftest get-file-directory-test
  (testing "The correct cannocial path is returned"
    (let [input-path "./resources"
          actual-value (util/get-file-directory input-path)
          root-folder (System/getProperty "user.dir")
          expected-value (str root-folder "/" "resources")]
      (is (= expected-value actual-value)))))

(deftest create-aux-folder-test
  (testing "The folder has beencreated succesfully"
    (util/create-aux-folder test-file-path)
    (let [file-exists? (.exists (io/file test-file-path))]
      (is  file-exists?))))

(deftest delete-aux-folder-test
  (testing "The aux-folder is succesfully deleted"
    (let [pre-existence-condition (.exists (nth test-paths 0))
          _ (util/delete-aux-folder (nth test-paths 0))
          post-existence-condition (.exists (nth test-paths 0))]
      (is (and pre-existence-condition (not post-existence-condition))))))
