(ns calculator-api.server.server_test
  (:require [clojure.test :refer :all]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [ring.middleware.params :as params]
            [ring.mock.request :as mock]
            [reitit.coercion.spec :as rcs]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as coercion]
            [calculator-api.server.server :refer [math-routes experimental-routes]]))


(def test-app
  (ring/ring-handler
    (ring/router
      [math-routes]

      {:data {:coercion   rcs/coercion
              :muuntaja   m/instance
              :middleware [params/wrap-params
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-request-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]}})))

(deftest math-routes
  (testing "GET /math/addition"
    (testing "x and y are added up"
      (let [{:keys [status body]} (test-app (-> (mock/request :get "/math/addition")
                                                (mock/query-string {:x 5 :y 5})))]
        (is (= 200 status))
        (is (= {:total 10} body)))))

  (testing "GET /math/subtract/:y/from/:x"
    (testing "y is subtracted from x"
      (let [{:keys [status body]} (test-app (-> (mock/request :get "/math/subtract/10/from/25")))]
        (is (= 200 status))
        (is (= {:difference 15} body)))))

  (testing "GET /math/subtraction"
    (testing "y is subtracted from x"
      (let [{:keys [status body]} (test-app (-> (mock/request :post "/math/subtraction")
                                                (mock/json-body {:x 10 :y 5})))]
        (is (= 200 status))
        (is (= {:difference 5} body)))))


  (testing "GET /math/division"
    (testing "x is divided by y"
      (let [{:keys [status body]} (test-app (-> (mock/request :post "/math/division")
                                                (mock/json-body {:x 15 :y 5})))]
        (is (= 200 status))
        (is (= {:quotient 3} body))))

    (testing "divide by zero throws an error"
      (let [{:keys [status]} (test-app (-> (mock/request :post "/math/division")
                                           (mock/json-body {:x 15 :y 0})))]
        (is (= 400 status)))))

  (testing "get /math/multiplication"
    (testing "x is multiplied by y"
      (let [{:keys [status body]} (test-app (-> (mock/request :post "/math/multiplication")
                                                (mock/json-body {:x 25 :y 3})))]
        (is (= 200 status))
        (is (= {:product 75} body))))))
