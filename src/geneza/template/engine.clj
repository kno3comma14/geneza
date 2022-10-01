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

(defn- extract-folders
  [project-structure-info]
  (keys (map name (:folder-structure project-structure-info))))

(defn build-project-hierarchy
  [project-structure-info]
  (let [folders (extract-folders project-structure-info)]
    (util/create-aux-folder-hierarchi folders)
    (doseq [f folders]
      (let [template-info (get-in project-structure-info [:folder-structure (keyword f)])
            template-file-url (str (:template-path template-info) (:template template-info))
            template-map (:template-map template-info)
            target-folder-path (:file-path template-info)
            template-name (:filename template-info)]
        (build-template template-file-url template-map target-folder-path template-name)))))
