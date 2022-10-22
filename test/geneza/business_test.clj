(ns geneza.business-test
  (:require [geneza.business :as business]
            [clojure.test :refer [deftest testing is]]
            [datomic.api :as d]))

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
                    :db/doc "List of actors associated to a movie {actor}"}])

(def expected-endpoints-info [{:resource "movie",
                               :endpoints [{:http-method :get, :uri "theater/movie"}
                                           {:http-method :get, :uri "theater/movie/{id}"}
                                           {:http-method :post, :uri "theater/movie"}
                                           {:http-method :put, :uri "theater/movie/{id}"}
                                           {:http-method :delete, :uri "theater/movie/{id}"}
                                           {:http-method :get, :uri "theater/actor/{id}/movie"}]}
                              {:resource "actor",
                               :endpoints [{:http-method :get, :uri "theater/actor"}
                                           {:http-method :get, :uri "theater/actor/{id}"}
                                           {:http-method :post, :uri "theater/actor"}
                                           {:http-method :put, :uri "theater/actor/{id}"}
                                           {:http-method :delete, :uri "theater/actor/{id}"}]}])

(def config {:application-name "theater-collection" ;; This will be the structure of the config object for future project
             :db-config {:type :in-memory
                         :schema-name "movie_data"
                         :uri-prefix "datomic:mem://"}
             :api-prefix "theater"})

(defn- create-db
  [id]
  (let [db-uri (str "datomic:mem://movie_data" id)
        created? (d/create-database db-uri)
        connection (when created?
                     (d/connect db-uri))]
    (when (not (nil? connection))
      (d/transact connection movie-schema))
    (d/db connection)))

(deftest collect-endpoints-test
  (testing "Collecting test endpoints (In-memory database)"
    (let [config {:db-config (create-db 4)
                  :api-prefix "theater"}
          actual-value (business/collect-endpoints config)
          expected-value expected-endpoints-info]
      (is (= actual-value expected-value)))))
