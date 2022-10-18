(ns geneza.template.dynamic.route-engine-test
  (:require [clojure.test :refer [deftest testing is]]
            [geneza.template.dynamic.route-engine :as route-engine]))

(deftest generate-source-routes-test
  (testing "Source is generated correctly"
    (let [routes-data {}
          expected-result ""
          actual-result (route-engine/generate-source-routes routes-data)]
      (is (= expected-result actual-result)))))
