(ns geneza.template.util
  (:require [clojure.java.io :as io]))

(defn get-file-directory
  "Returns canonical path of a given path"
  [path]
  (.getCanonicalPath (io/file path)))

(defn create-aux-folder
  [path]
  (.mkdir (java.io.File. path)))

(defn delete-aux-folder
  [input-directory]
  (when (.isDirectory input-directory)
    (run! delete-aux-folder (.listFiles input-directory)))
  (io/delete-file input-directory))

(defn create-aux-folder-hierarchi
  [folder-hierarchi]
  (doseq [folder folder-hierarchi]
    (create-aux-folder folder)))



