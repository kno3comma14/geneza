(ns geneza.template.manager
  (:require [geneza.template.engine :as engine]))


(defn build-project-base
  [project-name project-description project-version project-url]
  (let [project-structure-info {:folder-structure {"" [{:template "project.clj.template"
                                                        :template-path "kit"
                                                        :template-map {:project-name project-name
                                                                       :project-description project-description
                                                                       :project-version project-version
                                                                       :project-url project-url}
                                                        :file-path "resources/temp/project.clj"}
                                                       {:template "Dockerfile.template"
                                                        :template-path "kit"
                                                        :template-map {:application-name project-name}
                                                        :file-path "resources/temp/Dockerfile"}
                                                       {:template "Makefile.template"
                                                        :template-path "kit"
                                                        :template-map {}
                                                        :file-path "resources/temp/Makefile"}]}
                                :context {}}]
    ;; TODO fill the map with all project base templates
    (engine/build-project-hierarchy project-structure-info)))

(defn build-project-sources
  ;; TODO: Define arguments required => Implementation using engine namespace
  [])
