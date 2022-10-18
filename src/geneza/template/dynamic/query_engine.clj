(ns geneza.template.dynamic.query-engine)

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
    (reduce (fn [acc, item]
              (str acc (create-generic-tuple (:tuple-name item) (:attribute item)) "\n"))
            where-literal
            tuple-info-list)))

(defn create-generic-query-string
  [tuple-info-list]
  (let [attribute-list (map (fn [item] (:attribute item)) tuple-info-list)
        find-query-section (create-generic-find-query-section attribute-list)
        where-section (create-generic-where-section tuple-info-list)]
    (str find-query-section where-section "]")))
