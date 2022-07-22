(ns geneza.api.schema-analyzer
  (:require [geneza.db.datomic.util :as db-util]))


;; (def bla {:db/ident :actor/age,
;;           :db/valueType :db.type/long,
;;           :db/cardinality :db.cardinality/one,
;;           :db/doc "The age of the actor"})


;; ;; Structure wanted
;; (def structure {:entity-name "movie"
;;                 :attributes [{:type "string"
;;                               :name "name"}
;;                              {:type "string"
;;                               :name "description"}]
;;                 :relationships [{:entity "actor"
;;                                  :cardinality :many}]})

(defn datomic-attribute->geneza-attribute
  [datomic-attribute]
  (let [attribute-name (name (:db/ident datomic-attribute))
        attribute-type (name (:db/valueType datomic-attribute))]
    {:type attribute-type
     :name attribute-name}))

(defn build-attribues
  [schema resource]
  (let [datomic-attributes (filterv (fn [x] (= resource (namespace (:db/ident x)))) schema)]
    (mapv (fn [x] (datomic-attribute->geneza-attribute x)) datomic-attributes)))

(defn build-relationships
  [schema resource]
  (let [valid-attributes (filterv (fn [x] (not= resource (name (:db/ident x)))) schema)]
    (mapv (fn[x] {:entity (namespace (:db/ident x))
                 :cardinality (name (:db/cardinality x))})
          valid-attributes)))

(defn build-entity-data
  [db]
  (let [schema (db-util/extract-api-resources db)
        resources (db-util/extract-api-resources schema)
        result []]
    nil))

(defn generate-api-hierarchy
  [db]
  nil)

