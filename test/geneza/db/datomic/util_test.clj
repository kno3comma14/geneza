(ns geneza.db.datomic.util-test
  (:require [clojure.test :refer [deftest testing is]]
            [datomic.api :as d]
            [geneza.db.datomic.util :as db-util]))

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

(def schema-info
  [{:db/ident :movie/title,
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
    :db/doc "List of actors associated to a movie"}
   {:db/ident :actor/name,
    :db/valueType :db.type/string,
    :db/cardinality :db.cardinality/one,
    :db/doc "The name of the actor"}
   {:db/ident :actor/age,
    :db/valueType :db.type/long,
    :db/cardinality :db.cardinality/one,
    :db/doc "The age of the actor"}])

(defn- create-db
  []
  (let [db-uri "datomic:mem://movie_data00"
        created? (d/create-database db-uri)
        connection (when created?
                     (d/connect db-uri))]
    (when (not (nil? connection))
      (d/transact connection movie-schema))
    (d/db connection)))

(deftest extract-user-schema-test
  (testing "The schema is correctly created"
    (let [db (create-db)
          actual-schema (db-util/extract-user-schema db)
          expected-schema schema-info]
      (is (= expected-schema actual-schema)))))

(deftest extract-api-resources-test
  (testing "extract-api-resources is extracting the resources in correct way"
    (let [db-schema schema-info
          actual-result (db-util/extract-api-resources db-schema)
          expected-result #{"movie" "actor"}]
      (is (= expected-result actual-result)))))


