(ns geneza.template.dynamic.persistency-engine
  (:require [geneza.api.schema-analyzer :as schema-analyzer]))

(defn fetch-resource-by-id
  [entity-info id])

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
