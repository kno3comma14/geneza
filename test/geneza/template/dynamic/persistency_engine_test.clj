(ns geneza.template.dynamic.persistency-engine-test
  (:require [clojure.test :refer [deftest is testing]]
           [geneza.template.dynamic.persistency-engine :as pe]))

(deftest create-ns-header-test
  (testing "Correct behavior of create-ns-header function"
    (let [resource-name "karaota"
          application-name "karaota-manager"
          expected-value "(ns karaota-manager.persistence.karaota\n(:require [datomic.api :as datomic-api]))\n"
          actual-value (pe/create-ns-header resource-name application-name)]
      (is (= expected-value actual-value)))))

(deftest create-function-header-test
  (testing "Correct behavior of create-function-header function"
    (let [resource-name "karaota"
          function-key :fetch-all-resources
          expected-value "(defn fetch-all-karaota [db]\n"
          actual-value (pe/create-function-header resource-name function-key)]
      (is (= expected-value actual-value)))))

(deftest build-read-resources-function-test
  (testing "Correct behavior of read-resources function"
    (let [entity-info [{:tuple-name "karaota" :attribute "literal" :value nil}
                       {:tuple-name "karaota" :attribute "counter" :value nil}
                       {:tuple-name "karaota" :attribute "killer" :value nil}
                       {:tuple-name "karaota" :attribute "factor" :value nil}]
          resource-name "karaota"
          expected-value "(defn fetch-all-karaota [db]\n(d/q '[:find ?e ?literal ?counter ?killer ?factor\n:where\n[?e :karaota/literal ?literal]\n[?e :karaota/counter ?counter]\n[?e :karaota/killer ?killer]\n[?e :karaota/factor ?factor]\n] db))"
          actual-value (pe/build-read-resources-function entity-info resource-name)]
      (is (= expected-value actual-value)))))

(deftest fetch-resource-by-id-test)

(deftest soft-delete-resource-by-id-test)

(deftest delete-resource-by-id-test)

(deftest update-resource-by-id-test)

(deftest create-resource-test)



