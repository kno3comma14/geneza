(ns geneza.template.dynamic.transaction-engine)

(defn create-generic-update-header-section
  [entity-name]
  (format "update-%s-by-id [connection id attribute new-value]\n" entity-name))

(defn create-generic-update-body-section []
  "(let [entity (d/entity (d/db connection) id)\n
      current-val (get entity attribute 0)\n
      tx-value [[:db/add id attribute new-value]]]\n
  tx-value))")

(defn build-update-entity-command
  [entity-name]
  (str (create-generic-update-header-section entity-name)
       (create-generic-update-body-section)))
