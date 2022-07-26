(ns geneza.template.dynamic.query-engine
  (:require [geneza.template.util :as util]))

(defn create-generic-find-query-section
  "Creates a header for a query string following a Datalog structure."
  [attributes include-id?] ;; List of strings
  (let [attribute-part (reduce (fn [acc, item] (str acc " " (str "?" item)))
                               ""
                               attributes)]
    (if include-id?
      (str "'[:find ?eid" attribute-part " :in $ ?eid" "\n")
      (str "'[:find" attribute-part "\n"))))

(defn create-generic-tuple
  "Creates a generic tuple given tuple-name, attribute and an optional value argument."
  ([tuple-name attribute value]
   (let [entity "?eid"
         complete-attribute (str ":" tuple-name "/" attribute)
         literal-value (if (not= value nil)
                         (util/prepare-value value)
                         (str "?" attribute))]
     (if (not= value nil)
       (str "[" entity " " complete-attribute " " literal-value "]")
       (str "[" entity " " complete-attribute " " "?" attribute "]"))))
  ([tuple-name attribute]
   (let [entity "?eid"
         complete-attribute (str ":" tuple-name "/" attribute)
         literal-value "_"]
     (str "[" entity " " complete-attribute " " literal-value "]"))))

(defn create-generic-where-section
  "Creates a where section given tuple-info-list map"
  [tuple-info-list]
  (let [where-literal ":where\n"]
    (reduce (fn [acc, item]
              (if (contains? item :value)
                (str acc (create-generic-tuple (:tuple-name item) (:attribute item) (:value item)) "\n")
                (str acc (create-generic-tuple (:tuple-name item) (:attribute item)) "\n")))
            where-literal
            tuple-info-list)))

(defn create-generic-query-string
  "Creates a generic complete query given a tuple-info-lisp map"
  [tuple-info-list include-id?]
  (let [attribute-list (map (fn [item] (:attribute item)) tuple-info-list)
        find-query-section (create-generic-find-query-section attribute-list include-id?)
        where-section (create-generic-where-section tuple-info-list)]
    (str find-query-section where-section "]")))

