(ns geneza.template.engine
  (:require [selmer.parser :as parser]))

(defn build-template
  [template-file-url template-map]
  (parser/render-file template-file-url template-map))




