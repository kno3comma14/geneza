(ns geneza.template.engine
  (:require [selmer.parser :as parser]
            [clojure.java.io :as io :refer [file]]))

(defn get-file-directory
  "Returns canonical path of a given path"
  [path]
  (.getCanonicalPath (io/file path)))

(defn parse-template
  [template-file-url template-map]
  (parser/render-file template-file-url template-map))

(defn build-template
  [template-file-url template-map target-folder-path template-name]
  (let [pre-template (parse-template template-file-url template-map)
        built-path (get-file-directory target-folder-path)]
    (io/file built-path template-name)))

(defn delete-aux-folder
  [input-directory]
  (when (.isDirectory input-directory)
    (run! delete-aux-folder (.listFiles input-directory)))
  (io/delete-file input-directory))






