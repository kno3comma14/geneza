(ns geneza.template.dynamic.query-engine)

(defn- ->quotes-string
  "Transforms a target string to a quoted string."
  [target]
  (let [quote-wrapper "\""]
    (str quote-wrapper target quote-wrapper)))

(defn- prepare-value
  "Prepares a value depending on it's type."
  [value]
  (if (string? value)
    (->quotes-string value)
    value)) ;; Test pending

(defn create-generic-find-query-section
  "Creates a header for a query string following a Datalog structure."
  [attributes] ;; List of strings
  (let [attribute-part (reduce (fn [acc, item] (str acc " " (str "?" item)))
                               ""
                               attributes)]
    (str "'[:find" attribute-part "\n"))) ;; Test pending

(defn create-generic-tuple
  "Creates a generic tuple given tuple-name, attribute and an optional value argument."
  ([tuple-name attribute value]
   (let [entity "?e"
         complete-attribute (str ":" tuple-name "/" attribute)
         literal-value (if (not= value nil)
                         (prepare-value value)
                         (str "?" attribute))]
     (if (not= value nil)
       (str "[" entity " " complete-attribute " " literal-value "]")
       (str "[" entity " " complete-attribute " " "?" attribute))))
  ([tuple-name attribute]
   (let [entity "?e"
         complete-attribute (str ":" tuple-name "/" attribute)
         literal-value "_"]
     (str "[" entity " " complete-attribute " " literal-value "]")))) ;; Tests pending

(defn create-generic-where-section
  "Creates a where section given tuple-info-list map"
  [tuple-info-list]
  (let [where-literal ":where\n"]
    (reduce (fn [acc, item]
              (if (contains? item :value)
                (str acc (create-generic-tuple (:tuple-name item) (:attribute item) (:value item)) "\n")
                (str acc (create-generic-tuple (:tuple-name item) (:attribute item)) "\n")))
            where-literal
            tuple-info-list))) ;; Test pending

(defn create-generic-query-string
  "Creates a generic complete query given a tuple-info-lisp map"
  [tuple-info-list]
  (let [attribute-list (map (fn [item] (:attribute item)) tuple-info-list)
        find-query-section (create-generic-find-query-section attribute-list)
        where-section (create-generic-where-section tuple-info-list)]
    (str find-query-section where-section "]"))) ;; Test pending
