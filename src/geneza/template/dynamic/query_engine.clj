(ns geneza.template.dynamic.query-engine)

(defn- ->quotes-string
  [target]
  (let [quote-wrapper "\""]
    (str quote-wrapper target quote-wrapper)))

(defn- prepare-value
  [value]
  (if (string? value)
    (->quotes-string value)
    value)) ;; Test pending

(defn create-generic-find-query-section
  [attributes] ;; List of strings
  (let [attribute-part (reduce (fn [acc, item] (str acc " " (str "?" item)))
                               ""
                               attributes)]
    (str "'[:find" attribute-part "\n"))) ;; Test pending

(defn create-generic-tuple
  [tuple-name attribute value]
  (let [entity "?e"
        complete-attribute (str ":" tuple-name "/" attribute)
        literal-value (if (not= value nil)
                        (prepare-value value)
                        (str "?" attribute))]
    (if (not= value nil)
      (str "[" entity " " complete-attribute " " literal-value "]")
      (str "[" entity " " complete-attribute " " "?" attribute)))) ;; Test pending

(defn create-generic-where-section
  [tuple-info-list]
  (let [where-literal ":where\n"]
    (reduce (fn [acc, item]
              (str acc (create-generic-tuple (:tuple-name item) (:attribute item) (:value item)) "\n"))
            where-literal
            tuple-info-list))) ;; Test pending

(defn create-generic-query-string
  [tuple-info-list]
  (let [attribute-list (map (fn [item] (:attribute item)) tuple-info-list)
        find-query-section (create-generic-find-query-section attribute-list)
        where-section (create-generic-where-section tuple-info-list)]
    (str find-query-section where-section "]"))) ;; Test pending
