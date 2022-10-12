(ns geneza.business
  (:require [geneza.api.schema-analyzer :as schema-analyzer]))

;; Fist, collect all endpoints from input DB configuration using api and db namespaces
(defn collect-endpoints
  [config]
  (let [db (:db-config config)
        api-prefix (:api-prefix config)]
    (schema-analyzer/generate-api-hierarchy db api-prefix)))
;; Save project fundamental data in the geneza db
;; Second, Build the temporal project code using the templates
;; create git repository using github api (Future)
;; Deploy the code to the cloud uisng some IaaC sdks/apis (Future)
