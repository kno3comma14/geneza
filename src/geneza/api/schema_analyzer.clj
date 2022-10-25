(ns geneza.api.schema-analyzer
  (:require [geneza.db.datomic.util :as db-util]))

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
  (let [schema (db-util/extract-user-schema db)
        resources (db-util/extract-api-resources schema)]
    (mapv (fn [entity] {:entity-name entity
                         :attributes (build-attributes schema entity)})
          resources)))

(defn geneza-entity->geneza-endpoints
  [entity api-prefix]
  (let [resource-name (:entity-name entity)
        base-endpoint (str api-prefix "/" resource-name)
        basic-endpoints [{:http-method :get :uri base-endpoint}
                         {:http-method :get :uri (str base-endpoint "/" "{id}")}
                         {:http-method :post :uri base-endpoint}
                         {:http-method :put :uri (str base-endpoint "/" "{id}")}
                         {:http-method :delete :uri (str base-endpoint "/" "{id}")}]]
    (reduce (fn [acc, att] (let [reference (:ref att)]
                             (if (not= nil (:target reference))
                               (conj acc {:http-method :get
                                          :uri (str api-prefix "/" (:target reference) "/" "{id}" "/" resource-name)})
                               acc)))
            basic-endpoints
            (:attributes entity))))

(defn generate-api-hierarchy
  [db api-prefix]
  (let [entities-data (build-entity-data db)]
    (mapv (fn [entity] {:resource (:entity-name entity)
                        :endpoints (geneza-entity->geneza-endpoints entity api-prefix)})
          entities-data)))

