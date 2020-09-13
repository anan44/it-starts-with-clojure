(ns calculator-api.server.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [ring.util.http-response :as response]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec :as rcs]
            [reitit.ring :as ring]))

(def math-routes
  ["/math"
   ["/plus" {:get (fn [req]
                    (let [params (:query-params req)
                          x (Long/parseLong (get params "x"))
                          y (Long/parseLong (get params "y"))]
                      {:status 200
                       :body   {:total (+ x y)}}))}]

   ["/minus" {:post {:handler (fn [req]
                                (let [x (-> req :body-params :x)
                                      y (-> req :body-params :y)]
                                  (response/ok {:total (- x y)})))}}]


   ["/minusproper" {:post {:description ""
                           :coercion    rcs/coercion
                           :parameters  {:body {:x number?
                                                :y number?}}
                           :handler     (fn [req]
                                          (let [x (-> req :parameters :body :x)
                                                y (-> req :parameters :body :y)]
                                            (response/ok {:total (- x y)})))}}]

   ["/plusthing" {:post {:coercion rcs/coercion
                         :parameters {:body {:x number?
                                             :y number?}}
                         :responses {200 {:body {:total number?}}}
                         :handler (fn [req]
                                    (let [x (-> req :parameters :body :x)
                                          y (-> req :parameters :body :y)]
                                      (response/ok {:total (+ x y)})))}}]])

(def hello-routes
  ["/hello" {:get {:handler (fn [_]
                              (response/ok {:message "Hello World!"}))}}])

(def app
  (ring/ring-handler
    (ring/router
      [hello-routes
       math-routes]
      {:data {:muuntaja   m/instance
              :middleware [params/wrap-params
                           muuntaja/format-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]}})
    (ring/create-default-handler)))

(defonce running-server (atom nil))

(defn start
  []
  (when (nil? @running-server)
    (reset! running-server (jetty/run-jetty #'app {:port  3000
                                                   :join? false})))
  (println "Server running in port 3000"))

(defn stop
  []
  (when-not (nil? @running-server)
    (.stop @running-server)
    (reset! running-server nil))
  (println "Server stopped"))
