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
                    :db/doc "List of actors associated to a movie {actor}"}])

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
  [id]
  (let [db-uri (str "datomic:mem://movie_data" id)
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

(deftest build-entity-data-test
  (testing "Build entity data properly"
    (let [db (create-db "01")
          actual-value (analyzer/build-entity-data db)
          expected-value [{:entity-name "movie"
                           :attributes [{:type "string"
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
                                         :ref {:cardinality "many" :target "actor"}}]}
                          {:entity-name "actor"
                           :attributes [{:type "string"
                                         :name "name"
                                         :ref {:cardinality "one" :target nil}}
                                        {:type "long"
                                         :name "age"
                                         :ref {:cardinality "one" :target nil}}]}]]
      (is (= expected-value actual-value)))))

(deftest geneza-entity->geneza-endpoints-test
  (testing "Transform an entity to endpoints properly"
    (let [input-entity {:entity-name "movie"
                        :attributes [{:type "string"
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
                                      :ref {:cardinality "many" :target "actor"}}]}
          actual-value (analyzer/geneza-entity->geneza-endpoints input-entity "/api/v1")
          expected-value [{:http-method :get, :uri "/api/v1/movie"}
                          {:http-method :get, :uri "/api/v1/movie/{id}"}
                          {:http-method :post, :uri "/api/v1/movie"}
                          {:http-method :put, :uri "/api/v1/movie"}
                          {:http-method :delete, :uri "/api/v1/movie/{id}"}
                          {:http-method :get, :uri "/api/v1/actor/{id}/movie"}]]
      (is (= expected-value actual-value)))))

(deftest generate-api-hierarchy-test
  (testing "The api hierarchy is created properly"
    (let [db (create-db "02")
          api-prefix "/api/v1"
          actual-value (analyzer/generate-api-hierarchy db api-prefix)
          expected-value [{:resource "movie"
                           :endpoints [{:http-method :get
                                        :uri "/api/v1/movie"}
                                       {:http-method :get
                                        :uri "/api/v1/movie/{id}"}
                                       {:http-method :post
                                        :uri "/api/v1/movie"}
                                       {:http-method :put
                                        :uri "/api/v1/movie"}
                                       {:http-method :delete
                                        :uri "/api/v1/movie/{id}"}
                                       {:http-method :get
                                        :uri "/api/v1/actor/{id}/movie"}]}
                          {:resource "actor"
                           :endpoints [{:http-method :get
                                        :uri "/api/v1/actor"}
                                       {:http-method :get
                                        :uri "/api/v1/actor/{id}"}
                                       {:http-method :post
                                        :uri "/api/v1/actor"}
                                       {:http-method :put
                                        :uri "/api/v1/actor"}
                                       {:http-method :delete
                                        :uri "/api/v1/actor/{id}"}]}]]
      (is (= expected-value actual-value)))))



