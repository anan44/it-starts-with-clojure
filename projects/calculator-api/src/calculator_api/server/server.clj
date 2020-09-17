(ns calculator-api.server.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [ring.util.http-response :as response]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec :as rcs]
            [reitit.ring :as ring]))

(defn not-zero-number?
  [x]
  (and (number? x) ((complement zero?) x)))

(def math-routes
  ["/math"
   ["/addition" {:get (fn [request]
                        (let [params (:query-params request)
                              x (Long/parseLong (get params "x"))
                              y (Long/parseLong (get params "y"))]
                          (response/ok {:total (+ x y)})))}]

   ["/subtraction" {:post {:description "Returns x subtracted by y"
                           :handler     (fn [request]
                                          (let [x (-> request :body-params :x)
                                                y (-> request :body-params :y)]
                                            (response/ok {:difference (- x y)})))}}]
   ["/subtract/:y/from/:x" {:get {:description "Returns x subracted by y"
                                  :handler     (fn [request]
                                                 (let [x (-> request :path-params :x Long/parseLong)
                                                       y (-> request :path-params :y Long/parseLong)]
                                                   (response/ok {:difference (- x y)})))}}]
   ["/division" {:post {:description "Returns x divided by y."
                        :coercion    rcs/coercion
                        :parameters  {:body {:x number?
                                             :y not-zero-number?}}
                        :handler     (fn [req]
                                       (let [x (-> req :parameters :body :x)
                                             y (-> req :parameters :body :y)]
                                         (response/ok {:quotient (/ x y)})))}}]
   ["/multiplication" {:post {:description "Returns x multiplied by y"
                              :coercion    rcs/coercion
                              :parameters  {:body {:x number?
                                                   :y number?}}
                              :responses   {200 {:body {:product number?}}}
                              :handler     (fn [req]
                                             (let [x (-> req :parameters :body :x)
                                                   y (-> req :parameters :body :y)]
                                               (response/ok {:product (* x y)})))}}]])

(def hello-routes
  ["/hello" {:get {:handler (fn [_]
                              (response/ok {:message "Hello Reitit!"}))}}])

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
