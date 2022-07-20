(ns geneza.api.schema-analyser
  (:require [geneza.db.datomic.util :as db-util]))

(defn generate-api-hierarchy
  [db]
  (let [schema (db-util/extract-api-resources db)
        resources (db-util/extract-api-resources schema)]
    ;; TODO generate api hierarchy
    nil))

