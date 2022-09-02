(ns geneza.template.util-test
  (:require [clojure.test :refer [deftest testing is]]
            [geneza.template.util :as util]))

(deftest get-file-directory-test
  (testing "The correct cannocial path is returned"
    (let [input-path "./resources"
          actual-value (util/get-file-directory input-path)
          root-folder (System/getProperty "user.dir")
          expected-value (str root-folder "/" "resources")]
      (is (= expected-value actual-value)))))
