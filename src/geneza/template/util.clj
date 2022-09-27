(ns geneza.template.util
  (:require [clojure.java.io :as io]))

(defn get-file-directory
  "Returns canonical path of a given path"
  [path]
  (str (.getCanonicalPath (io/file path)) "/"))

(defn create-aux-folder
  [path]
  (.mkdir (if (string? path)
            (io/file path)
            path)))

(defn delete-aux-folder
  [target-file]
  (let [tf (if (string? target-file)
             (io/file target-file)
             target-file)]
    (when (.isDirectory tf)
      (run! delete-aux-folder (.listFiles tf)))
    (io/delete-file tf)))

(defn create-aux-folder-hierarchi
  [folder-hierarchi]
  (doseq [folder folder-hierarchi]
    (create-aux-folder folder)))



