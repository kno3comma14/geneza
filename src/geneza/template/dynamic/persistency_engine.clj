(ns geneza.template.dynamic.persistency-engine
  (:require [geneza.api.schema-analyzer :as schema-analyzer]
            [geneza.template.dynamic.query-engine :as qe]))

(def generic-require-block "(:require [datomic.api :as datomic-api])")

(defn create-ns-header
  [resource-name application-name]
  (let [ns-start (str "ns " application-name ".persistency." resource-name "\n")
        ns-final (str generic-require-block ")\n")]
    (str ns-start ns-final)))

(defn fetch-resource-by-id
  [entity-info resource-name]
  (let [query (qe/create-generic-query-string entity-info)]))

(defn read-resources
  [resource-info-map])

(defn softdelete-resource-by-id
  [resource-info-map id])

(defn delete-resource-by-id
  [resource-info-map id])

(defn update-resource-by-id
  [operation-map])

(defn create-resource
  [resource-info-map])
