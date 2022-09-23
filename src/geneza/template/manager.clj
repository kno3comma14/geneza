(ns geneza.template.manager
  (:require [geneza.template.engine :as engine]))

(def base-url "")

(defn build-dockerfile
  [template-map]
  (let [template-file-url (str base-url "/resources/templates/kit/Dockerfile.template")
        target-folder-path (str base-url "/resources/temp/" (:application-name template-map))
        template-name "Dockerfile"]
    (engine/build-template template-file-url
                           template-map
                           target-folder-path
                           template-name)))

(defn build-project-clj
  [template-map]
  (let [template-file-url (str base-url "/resources/templates/kit/project.clj.template")
        target-folder-path (str base-url "/resources/temp/" (:application-name template-map))
        template-name "Dockerfile"]
    (engine/build-template template-file-url
                           template-map
                           target-folder-path
                           template-name)))

(defn build-makefile
  [template-map]
  (let [template-file-url (str base-url "/resources/templates/kit/Makefile.template")
        target-folder-path (str base-url "/resources/temp/" (:application-name template-map))
        template-name "Makefile"]
    (engine/build-template template-file-url
                           template-map
                           target-folder-path
                           template-name)))

(defn babashka-edn
  [template-map]
  (let [template-file-url (str base-url "/resources/templates/kit/bb.edn.template")
        target-folder-path (str base-url "/resources/temp/" (:application-name template-map))
        template-name "bb.edn"]
    (engine/build-template template-file-url
                           template-map
                           target-folder-path
                           template-name)))


(defn build-main
  [template-map]
  (let [template-file-url (str base-url "/resources/templates/kit/build.clj.template")
        target-folder-path (str base-url "/resources/temp/" (:application-name template-map))
        template-name "build.clj"]
    (engine/build-template template-file-url
                           template-map
                           target-folder-path
                           template-name)))
