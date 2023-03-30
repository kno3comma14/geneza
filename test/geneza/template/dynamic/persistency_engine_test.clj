(ns geneza.template.dynamic.persistency-engine-test
  (:require [clojure.test :refer [deftest is testing]]
           [geneza.template.dynamic.persistency-engine :as pe]))

(deftest create-ns-header-test
  (testing "Correct behavior of create-ns-header function"
    (let [resource-name "karaota"
          application-name "karaota-manager"
          expected-value "(ns karaota-manager.persistence.karaota\n(:require [datomic.api :as d]))\n"
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
          expected-value "(defn fetch-all-karaota [db]\n(d/q '[:find ?eid ?literal ?counter ?killer ?factor :in $ ?eid\n:where\n[?eid :karaota/literal ?literal]\n[?eid :karaota/counter ?counter]\n[?eid :karaota/killer ?killer]\n[?eid :karaota/factor ?factor]\n] db))"
          actual-value (pe/build-read-resources-function entity-info resource-name)]
      (is (= expected-value actual-value)))))

(deftest build-fetch-resource-by-id-function-test
  (testing "Correct behavior of build-fetch-resource-by-id-function function"
    (let [entity-info [{:tuple-name "karaota" :attribute "literal" :value nil}
                       {:tuple-name "karaota" :attribute "counter" :value nil}
                       {:tuple-name "karaota" :attribute "killer" :value nil}
                       {:tuple-name "karaota" :attribute "factor" :value nil}]
          resource-name "karaota"
          expected-value "(defn fetch-karaota-by-id [db id]\n(d/q '[:find ?eid ?literal ?counter ?killer ?factor :in $ ?eid\n:where\n[?eid :karaota/literal ?literal]\n[?eid :karaota/counter ?counter]\n[?eid :karaota/killer ?killer]\n[?eid :karaota/factor ?factor]\n] db id)"
          actual-value (pe/build-fetch-resource-by-id-function entity-info resource-name)]
      (is (= expected-value actual-value)))))

(deftest build-update-resources-by-id-function-test
  (testing "Correct behavior of build-update-resources-by-id-function function"
    (let [resource-name "karaota"
          expected-value "(defn update-karaota-by-id [connection id attribute new-value]\n(let [entity (d/entity (d/db connection) id)\n\n      tx-value [[:db/add id attribute new-value]]]\n\n  tx-value))"
          actual-value (pe/build-update-resource-by-id-function resource-name)]
      (is (= expected-value actual-value)))))

(deftest build-create-resource-function-test
  (testing "Correct behavior of build-create-resource-function function"
    (let [resource-name "karaota"
          expected-value "(defn create-karaota [entity-data connection]\n(d/transact connection entity-data)"
          actual-value (pe/build-create-resource-function resource-name)]
      (is (= expected-value actual-value)))))

(deftest build-softdelete-resource-by-id-function-test
  (testing "Correct behavior of build-softdelete-resource-by-id-function function"
    (let [resource-name "karaota"
          expected-value "(defn soft-delete-karaota-by-id [connection id delete-flag]\n(let [entity (d/entity (d/db connection) id)\n\n      tx-value [[:db/add id delete-flag false]]]\n\n  tx-value))"
          actual-value (pe/build-softdelete-resource-by-id-function resource-name)]
      (is (= expected-value actual-value)))))

(deftest build-delete-resource-by-id-function-test
  (testing "Correct behavior of build-delete-resource-by-id-function function"
    (let [resource-name "karaota"
          expected-value "(defn delete-karaota-by-id [id connection]\n(d/transact connection [[:db.fn/retractEntity id]])"
          actual-value (pe/build-delete-resource-by-id-function resource-name)]
      (is (= expected-value actual-value)))))


