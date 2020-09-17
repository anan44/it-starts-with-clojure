# 6.7. Response Coercion

So far we have now only coerced the request data,
but that is not where this ends.
Our setup allow us to coerce the response data as well.

So what does that actually mean coercion of response data?
Well basically we can tell our application what kinds of responses we intend our application to return.
If the response does not match any of the predefined responses,
we get an exception.
This is a nice way to make sure that our responses are indeed exactly what we expected of them to be.
Furthermore, it acts as a nice documentation.

So let's make a multiplication endpoint.

```clojure
["/multiplication" {:post {:description "Returns x multiplied by y"
                           :coercion    rcs/coercion
                           :parameters  {:body {:x number?
                                                :y number?}}
                           :responses   {200 {:body {:product number?}}}
                           :handler     (fn [req]
                                          (let [x (-> req :parameters :body :x)
                                                y (-> req :parameters :body :y)]
                                            (response/ok {:total (* x y)})))}}]
```

So again we have a rather familiar looking setup,
but with single new key present.
This time we have introduced `:responses`.
Behind that seems to be a map with numbers as keys.
These numbers represent HTTP status codes and those numbers are followed by maps that look suspiciously much like specs.
This spec specifies the shape of our response on HTTP code 200 (that is same as OK).

So let's give this endpoint a go:

```sh
curl --request POST \
  --url http://localhost:3000/math/multiplication \
  --header 'content-type: application/json' \
  --data '{
    "x": 10,
    "y": 15.5
}'
```

Again I added a bit of formation to the response:

```json
{
  "spec": "(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$8384/product]), :type :map, :leaf? false})",
  "problems": [
    {
      "path": [],
      "pred": "(clojure.core/fn [%] (clojure.core/contains? % :product))",
      "val": {
        "total": 155.0
      },
      "via": [],
      "in": []
    }
  ],
  "type": "reitit.coercion/response-coercion",
  "coercion": "spec",
  "value": {
    "total": 155.0
  },
  "in": [
    "response",
    "body"
  ]
}
```

After investigating the response a bit,
we can see that there seems to be an issue with our response body.
It seems like the response was supposed to contain `:product`.

After quick review on our code,
we seem to be having a mistake in the way how we formed our response.

```clojure
(response/ok {:total (* x y)})
```

It looks like we accidentally named our response value in the body `:total`.
Lets fix this and try again.

```clojure
(response/ok {:product (* x y)})
```

Let's try it again.
This time the response seems to be fine:

```json
{
  "product": 155.0
}
```

So here we saw the response coercion in action.
It helps us to catch silly mistakes like that.
We could also specify different shapes for different response codes.
For example different exceptions might have their own specs.
Using both request and response coercions can be really handy.
Utilizing them both helps us to overcome some of the short coming of Clojure's dynamic typing.
Yet in the end is up to you as the developer to decide if you will utilize these tools or not.

We have now gone through much of the basics required for building web application with Clojure using Ring & Reitit.
Some of the stuff is probably even beyond the basics.
This is because I really wanted to show you the power of Clojure in web application development.

Our application should now look something like this:

```clojure
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
```

This software was great for teaching these concepts to you,
but it also is a bit of a mess.
But this time we might just let that slide.
Not every application has to be pretty or useful,
especially when we are learning.

Before we leave this mess behind,
let's learn how to test this kind of application.

Next: [Testing Web Applications](8-testing-web-applications.md)