(ns geneza.template.dynamic.persistency-engine
  (:require [geneza.template.dynamic.query-engine :as qe]))

(def generic-require-block "(:require [datomic.api :as d])")

(def function-templates {:fetch-resource-by-id {:header "(defn fetch-%s-by-id [db id]\n"
                                                :body "(d/q %s db id)"}
                         :fetch-all-resources {:header "(defn fetch-all-%s [db]\n"
                                               :body "(d/q %s db))"}
                         :soft-delete-resource-by-id {:header "(defn soft-delete-%s-by-id [connection id]\n"
                                                      :body ""}
                         :delete-resource-by-id {:header "(defn delete-%s-by-id [id connection]\n"
                                                 :body ""}
                         :update-resource-by-id {:header "(defn update-%s-by-id [id entity-data connection]\n"
                                                 :body ""}
                         :create-resource {:header "(defn create-%s [entity-data connection]\n"
                                           :body ""}})

(defn create-ns-header
  [resource-name application-name]
  (let [ns-start (str "(ns " application-name ".persistence." resource-name "\n")
        ns-final (str generic-require-block ")\n")]
    (str ns-start ns-final)))

(defn create-function-header
  [resource-name function-key]
  (format (get-in function-templates [function-key :header]) resource-name))

(defn build-fetch-resource-by-id-function
  [entity-info resource-name]
  (let [query (qe/create-generic-query-string entity-info true)
        fn-header (format (get-in function-templates [:fetch-resource-by-id :header]) resource-name)
        fn-body (format (get-in function-templates [:fetch-resource-by-id :body]) query)]
    (str fn-header fn-body)))

(defn build-read-resources-function
  [entity-info resource-name]
  (let [query (qe/create-generic-query-string entity-info true)
        fn-header (format (get-in function-templates [:fetch-all-resources :header]) resource-name)
        fn-body (format (get-in function-templates [:fetch-all-resources :body]) query)]
    (str fn-header fn-body)))

(defn build-update-resource-by-id-function
  [resource-info-map])

(defn build-softdelete-resource-by-id-function
  [resource-info-map id])

(defn build-delete-resource-by-id-function
  [resource-info-map id])

(defn build-create-resource-function
  [resource-info-map])

(comment
  (d/transact
   conn
   [{:db/id #db/id [:db.part/user]
     :user/firstName "Johnathan2"}]))
