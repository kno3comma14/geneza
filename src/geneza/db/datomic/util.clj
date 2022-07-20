(ns geneza.db.datomic.util
  (:require [datomic.api :as d]))

(defn extract-user-schema
  [db]
  (vec (->> (d/q '[:find ?e
                   :where
                   [?e :db/ident ?ident]
                   [(namespace ?ident) ?ns]
                   (not (or [(contains? #{"db" "fressian"} ?ns)]
                            [(.startsWith ?ns "db.")]))]
                 db)
            (map #(->> % first (d/entity db) d/touch (into {}))))))

(defn extract-api-resources
  [db-schema]
  (set (map (fn [x] (namespace (:db/ident x))) db-schema)))

