(ns geneza.template.dynamic.query-engine-test
  (:require [clojure.test :refer [deftest testing is]]
            [geneza.template.dynamic.query-engine :as query-engine]))

(deftest create-generic-find-query-section-test
  (testing "Correct behavior of create-generic-find-query-section function - query-id? true"
    (let [attributes '("a" "b" "c")
          include-id? true
          expected-value "'[:find ?eid ?a ?b ?c :in $ ?eid\n"
          actual-value (query-engine/create-generic-find-query-section attributes include-id?)]
      (is (= expected-value actual-value))))
  (testing "Correct behavior of create-generic-find-query-section function - query-id? true"
    (let [attributes '("a" "b" "c")
          include-id? false
          expected-value "'[:find ?a ?b ?c\n"
          actual-value (query-engine/create-generic-find-query-section attributes include-id?)]
      (is (= expected-value actual-value)))))

(deftest create-generic-tuple
  (testing "Correct behavior of create-generic-tuple function - Complete case, string attribute, no binding"
    (let [tuple-name "tuple1"
          attribute "attribute1"
          value "value1"
          expected-value "[?eid :tuple1/attribute1 \"value1\"]"
          actual-value (query-engine/create-generic-tuple tuple-name attribute value)]
      (is (= expected-value actual-value))))
  (testing "Correct behavior of create-generic-tuple function - Complete case, int attribute, no binding"
    (let [tuple-name "tuple2"
          attribute "attribute2"
          value 1000
          expected-value "[?eid :tuple2/attribute2 1000]"
          actual-value (query-engine/create-generic-tuple tuple-name attribute value)]
      (is (= expected-value actual-value))))
  (testing "Correct behavior of create-generic-tuple function - Complete case, int attribute, attribute binding"
    (let [tuple-name "tuple3"
          attribute "attribute3"
          value nil
          expected-value "[?eid :tuple3/attribute3 ?attribute3]"
          actual-value (query-engine/create-generic-tuple tuple-name attribute value)]
      (is (= expected-value actual-value))))
  (testing "Correct behavior of create-generic-tuple function - Two attributes case, no attribute value"
    (let [tuple-name "tuple3"
          attribute "attribute3"
          expected-value "[?eid :tuple3/attribute3 _]"
          actual-value (query-engine/create-generic-tuple tuple-name attribute)]
      (is (= expected-value actual-value)))))

(deftest create-generic-where-section-test
  (testing "Correct behavior of create-generic-where-section function"
    (let [tuple-info-list [{:tuple-name "bla" :attribute "literal" :value nil}
                           {:tuple-name "bleh" :attribute "counter" :value "one"}
                           {:tuple-name "bleh" :attribute "killer" :value 1}
                           {:tuple-name "blah" :attribute "factor"}]
          expected-value ":where\n[?eid :bla/literal ?literal]\n[?eid :bleh/counter \"one\"]\n[?eid :bleh/killer 1]\n[?eid :blah/factor _]\n"
          actual-value (query-engine/create-generic-where-section tuple-info-list)]
      (is (= expected-value actual-value)))))

(deftest create-generic-query-string-test
  (testing "Correct behavior of create-generic-query-string function"
    (let [tuple-info-list [{:tuple-name "bla" :attribute "literal" :value nil}
                           {:tuple-name "bleh" :attribute "counter" :value "one"}
                           {:tuple-name "bleh" :attribute "killer" :value 1}
                           {:tuple-name "blah" :attribute "factor"}]
          include-id? false
          expected-value "'[:find ?literal ?counter ?killer ?factor\n:where\n[?eid :bla/literal ?literal]\n[?eid :bleh/counter \"one\"]\n[?eid :bleh/killer 1]\n[?eid :blah/factor _]\n]"
          actual-value (query-engine/create-generic-query-string tuple-info-list include-id?)]
      (is (= expected-value actual-value)))))
