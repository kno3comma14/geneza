(ns geneza.api.schema-analyzer-test
  (:require [clojure.test :refer [deftest testing is]]
            [datomic.api :as d]
            [geneza.api.schema-analyzer :as analyzer]))

(def movie-schema [{:db/ident :actor/name
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The name of the actor"}

                   {:db/ident :actor/age
                    :db/valueType :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc "The age of the actor"}

                   {:db/ident :movie/title
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The title of the movie"}

                   {:db/ident :movie/genre
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The genre of the movie"}

                   {:db/ident :movie/release-year
                    :db/valueType :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc "The year the movie was released in theaters"}

                   {:db/ident :movie/actor-list
                    :db/valueType :db.type/ref
                    :db/cardinality :db.cardinality/many
                    :db/doc "List of actors associated to a movie"}])

(def schema-info [{:db/ident :movie/title,
                   :db/valueType :db.type/string,
                   :db/cardinality :db.cardinality/one,
                   :db/doc "The title of the movie"}
                  {:db/ident :movie/genre,
                   :db/valueType :db.type/string,
                   :db/cardinality :db.cardinality/one,
                   :db/doc "The genre of the movie"}
                  {:db/ident :movie/release-year,
                   :db/valueType :db.type/long,
                   :db/cardinality :db.cardinality/one,
                   :db/doc "The year the movie was released in theaters"}
                  {:db/ident :movie/actor-list,
                   :db/valueType :db.type/ref,
                   :db/cardinality :db.cardinality/many,
                   :db/doc "List of actors associated to a movie {actor}"}
                  {:db/ident :actor/name,
                   :db/valueType :db.type/string,
                   :db/cardinality :db.cardinality/one,
                   :db/doc "The name of the actor"}
                  {:db/ident :actor/age,
                   :db/valueType :db.type/long,
                   :db/cardinality :db.cardinality/one,
                   :db/doc "The age of the actor"}])

(def movie-attribute {:db/ident :movie/title,
                      :db/valueType :db.type/string,
                      :db/cardinality :db.cardinality/one,
                      :db/doc "The title of the movie"})

(defn- create-db
  []
  (let [db-uri "datomic:mem://movie_data"
        created? (d/create-database db-uri)
        connection (when created?
                     (d/connect db-uri))]
    (when (not (nil? connection))
      (d/transact connection movie-schema))
    (d/db connection)))

(deftest datomic-attribute->geneza-attribute-test
  (testing "Geneza attributes are being created properly"
    (let [input-attribute movie-attribute
          actual-value (analyzer/datomic-attribute->geneza-attribute input-attribute)
          expected-value {:type "string"
                          :name "title"
                          :ref {:cardinality "one" :target nil}}]
      (is (= expected-value actual-value)))))

(deftest build-attributes-test
  (testing "Geneza attributes created properly for movie resource"
    (let [schema schema-info
          actual-value (analyzer/build-attributes schema "movie")
          expected-value [{:type "string"
                           :name "title"
                           :ref {:cardinality "one" :target nil}}
                          {:type "string"
                           :name "genre"
                           :ref {:cardinality "one" :target nil}}
                          {:type "long"
                           :name "release-year"
                           :ref {:cardinality "one" :target nil}}
                          {:type "ref"
                           :name "actor-list"
                           :ref {:cardinality "many" :target "actor"}}]]
      (is (= expected-value actual-value)))))



