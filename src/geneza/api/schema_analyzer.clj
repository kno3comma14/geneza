(ns geneza.api.schema-analyzer
  (:require [geneza.db.datomic.util :as db-util]))


;; (def bla {:db/ident :actor/age,
;;           :db/valueType :db.type/long,
;;           :db/cardinality :db.cardinality/one,
;;           :db/doc "The age of the actor"})


;; ;; sample Structure wanted
;; (def structure {:entity-name "movie"
;;                 :attributes [{:type "string"
;;                               :name "name"
;;                               :ref {:cardinality one :target nil}}
;;                              {:type "string"
;;                               :name "description"
;;                               :ref {:cardinality one :target nil}}]})

(defn- clean-curlies
  [target]
  (subs target 1 (dec (count target))))

(defn extract-target-entity
  [target-doc]
  (let [matcher (re-matcher #"\{\w+\}" target-doc)
        possible-result (re-find matcher)]
    (when (not (nil? possible-result))
      (clean-curlies possible-result))))

(defn datomic-attribute->geneza-attribute
  [datomic-attribute]
  (let [attribute-name (name (:db/ident datomic-attribute))
        attribute-type (name (:db/valueType datomic-attribute))
        attribute-cardinality (name (:db/cardinality datomic-attribute))
        attribute-ref-target (extract-target-entity (:db/doc datomic-attribute))]
    {:type attribute-type
     :name attribute-name
     :ref {:cardinality attribute-cardinality :target attribute-ref-target}}))

(defn build-attributes
  [schema resource]
  (let [datomic-attributes (filterv (fn [x] (= resource (namespace (:db/ident x)))) schema)]
    (mapv (fn [x] (datomic-attribute->geneza-attribute x)) datomic-attributes)))

(defn build-entity-data
  [db]
  (let [schema (db-util/extract-api-resources db)
        resources (db-util/extract-api-resources schema)
        result []]
    nil))

(defn generate-api-hierarchy
  [db]
  nil)

