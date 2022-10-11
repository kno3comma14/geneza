(ns geneza.template.engine
  (:require [selmer.parser :as parser]
            [geneza.template.util :as util]
            [clojure.java.io :as io]))

(parser/set-resource-path! (clojure.java.io/resource "templates"))

(defn parse-template
  [template-file-url template-map]
  (parser/render-file  template-file-url template-map))

(defn build-template
  [template-file-url template-map target-folder-path]
  (let [pre-template (parse-template template-file-url template-map)
        built-path (util/get-file-directory target-folder-path)]
    (with-open [w (io/writer built-path)]
      (.write w pre-template))))

(defn- extract-folders
  [project-structure-info]
  (keys (:folder-structure project-structure-info)))

(defn build-project-hierarchy
  [project-structure-info]
  (let [folders (extract-folders project-structure-info)
        folders-to-build (filter (fn [x] (seq x)) folders)]
    (util/create-aux-folder-hierarchi (map (fn [x] (str (util/get-file-directory "") "/resources/temp/" x)) folders-to-build))
    (doseq [f folders]
      (doseq [template-info (get-in project-structure-info [:folder-structure f])]
        (let [template-file-url (str (:template-path template-info) "/" (:template template-info))
              template-map (:template-map template-info)
              target-folder-path (:file-path template-info)]
          (build-template template-file-url template-map target-folder-path))))))
