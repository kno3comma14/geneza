(ns geneza.template.dynamic.route-engine)

(defn create-generic-find-query-section
  [attributes] ;; List of strings
  (let [attribute-part (reduce (fn [acc, item] (str acc " " (str "?" item)))
                               ""
                               attributes)]
    (str "'[:find" attribute-part "\n")))

(defn create-generic-tuple
  [tuple-name attribute]
  (let [entity "?e"
        complete-attribute (str ":" tuple-name "/" attribute)
        value (str "?" attribute)]
    (str "[" entity " " complete-attribute " " value "]")))

(defn create-generic-where-section
  [tuple-info-list]
  (let [where-literal ":where\n"]
    (reduce (fn [acc, item] (str acc (create-generic-tuple (:tuple-name item) (:attribute item))))
            where-literal
            tuple-info-list)))

(defn resource->entity-query
  [resource api-prefix]
  (let [name (:name resource)
        entity (:entity resource)]
    ))

(defn generate-single-resource-query
  "resource-info structure:
  {:main-resource :string
   :cardinality :enum[one, many]
   :subresources [{:name :string
                   :entity :string}]}"
  [resource-info]
  ())

(defn generate-many-resources-query
  [resource-list cardinality])

(defn generate-source-routes
  [routes-data])
