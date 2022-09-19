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
