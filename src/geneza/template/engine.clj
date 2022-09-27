(ns geneza.template.engine
  (:require [selmer.parser :as parser]
            [geneza.template.util :as util]
            [clojure.java.io :as io]))

(parser/set-resource-path! (clojure.java.io/resource "templates"))

(defn parse-template
  [template-file-url template-map]
  (parser/render-file  template-file-url template-map))

(defn build-template
  [template-file-url template-map target-folder-path template-name]
  (let [pre-template (parse-template template-file-url template-map)
        built-path (util/get-file-directory target-folder-path)]
    (with-open [w (io/writer (str built-path template-name))]
      (.write w pre-template))))

