# 6.2 Hello Reitit

In the previous section we wrote some boiler plate,
so at this point your application should look like something like this:

```clojure
(ns calculator-api.server.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [ring.util.http-response :as response]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]))

(def app
  (ring/ring-handler
    (ring/router
      []
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

Now we will be building our very first (and very unimpressive) hello-reitit route.

## Your First Route

There is again many ways to do this and for sure every Clojure Web Developer has their own favorite way.
Obviously larger applications require more structure,
but with our tiny application this structure should suffice.

Basically reitit routers consist of basic Clojure data structures,
so if need to be they can be created programmatically or built up from smaller data structures.
This is all really up to you as the developer to decide.

So let's get down to business.

Our goal is to create a route that always returns a data structure with message stating "Hello Reitit"

We want to define hello-routes data structure.
You can consider this as independent group of routes,
which now happens to have only single route.

A single route data structure is a vector that contains 2 items:

- Route Path (string)
- Route Data (map or vector)

Route Path is a simple string describing the path.
This string may contain **path parameters** like "/user/:id",
but obviously those are not mandatory.

Route Data is data structure describing the route and its behavior.
It contains things like methods, handlers, and descriptions.
Route Data can also contain many other fields and is definitely limited to these.

Our hello-route is rather bare minimum required from a route.

```clojure
(def hello-routes
  ["/hello" {:get {:handler (fn [_]
                              (response/ok {:message "Hello Reitit!"}))}}])
```

Let's bisect it a bit.
We define a regular vector with two items on it.
The string `"/hello"` is the path we are responding from,
and the data structure specifies how is the response going to look like.

The Route Data contains keywords representing the accepted HTTP methods.
(If you are not familiar with HTTP methods please checkout [developer.mozzila.org](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods))
In our case we only support HTTP method `get`.

For `get` methods to path `/hello` we run the following handler function:

```clojure
(fn [_]
  (response/ok {:message "Hello Reitit!"}))
```

Handler functions take [Ring request](https://github.com/ring-clojure/ring/wiki/Concepts#requests) as single argument.
The request contains bunch of details by default,
and more can be added by using middleware.
Since in our /hello path will return always the same response,
we won't be needing the request for anything.
Because we are not using the request,
we name it `_` so we will not have unused variables in our code.
Replacing unused variables with `_` is a common practice in Clojure,
since it reduces noise in our code and makes it more readable.

`response/ok` wraps our response into format expected by Ring.
Try following call in your REPL and you will see that response is just a normal map.

```clojure
(response/ok {:message "Hello Reitit!"})
;=> {:status 200, :headers {}, :body {:message "Hello Reitit!"}}
```

You might be starting to see pattern here.
Almost everything in Clojure web development is just normal Clojure data structures.
There is very little magic going on that we need to be dealing with.

`response/ok` adds status 200, {} as headers and places our response inside the body.
Nothing forces us to use `response/ok`.
We could just as easily hard code this into our route or implement our own solution,
but since that is kind of a drag it is rather common to see utils modules like this being used.

In short these two handlers work identically:

```clojure
(fn [_]
  {:status  200
   :headers {}
   :body    {:message "Hello Reitit!"}})

```

```clojure
(fn [_]
  (response/ok {:message "Hello Reitit!"})
```

In the end it is up to you to decide which way you prefer.

Now that we have written `hello-routes` we need to connect it to our router.
To achieve this all we need to do is to add `hello-routes` into the empty vector inside `ring/router` in our app.

Like this:

```clojure
(def app
  (ring/ring-handler
    (ring/router
      [hello-routes]
      {:data {:muuntaja   m/instance
              :middleware [params/wrap-params
                           muuntaja/format-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]}})
    (ring/create-default-handler)))
```

Again this is a just a normal data structure,
so nothing would stop us from writing the definition directly inside `ring/router`,
and it can often be seen to be done in this way.
With that being said,
I would advice strongly against doing so.
It might feel like fast & furious to do so.
After all you would be writing less code.
But in doing so we would make application pain in the ass to refactor into multiple files if it grows larger.
Few lines of code is a small price to pay for clarity and easier future refactoring.
Good code is always easier to change.

## Changing namespaces

With this being done we are almost ready to start our server in REPL and see if all this works.
Since we are writing all this code outside core.clj,
we need to first switch our namespace to server.clj.

To switch namespace we can call write following call to REPL:

```clojure
(in-ns 'calculator-api.server.server)
```

This works OK,
but it is rather annoying especially in larger projects with multiple namespaces.
Thus I would recommend using shortcuts in your IDE.
All reasonable Clojure IDEs offer a handy way for switching the namespaces.

In Calva this is done with:
`ctrl+alt+v n`(linux + windows)
`cmd+alt+v n` (mac os)

In Cursive this is done with:
`ctrl-shift-n` (linux + windows)
`cmd-shift-n` (mac os)

These shortcuts might change so if these are not working check the command from menus.

## Starting server

Now that we have successfully switched the namespace in our REPL,
we can start our server.
This can be done following function call:

```clojure
(start)
```

After the server is running,
you can navigate to `http://localhost:3000/hello`in your browser.
If everything worked well,
you should be seeing something like this:

```json
{"message":"Hello Reitit!"}
```

Testing HTTP calls can be done in browser,
but after very simple HTTP Get calls we will soon hit a wall with our browser.
To dive deeper we need to use either [curl](https://curl.haxx.se/) or some other tool.

If you are using mac or linux computer you should already have curl installed by default.
For windows you might have to download it.

With curl we can call fo this same path with following command:

```sh
curl --request GET --url http://localhost:3000/hello
```

If you don't have much love command line tools,
I would recommend that you give a try to one of the following:

- [Insomnia Core](https://insomnia.rest/)
- [Postman](https://www.postman.com/)
- [Visual Studio Code REST Client plugin](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)

## Muuntaja in action

Let's checkout what muuntaja can do.
This part is will not involve any coding,
but I want to show it to you since it is quite cool.

As you noticed our application responded with JSON,
even though we returned Clojure data structure.
It is thanks to muuntaja that our application transforms this data into JSON,
instead of responding in [EDN](https://github.com/edn-format/edn).

But what if the some of the applications using our API would be written in Clojure,
and would prefer receiving EDN instead of JSON?
Well Muuntaja can service that need too.

Try adding accept: application/edn header to your http request.

```sh
curl --request GET \
  --url http://localhost:3000/hello \
  --header 'accept: application/edn'
```

You should receive a response like this:

```edn
{:message "Hello Reitit!"}
```

This finishes this section.
In the next section we will start building a simple calculator API.

Next up: [Query Parameters](3-query-parameters.md)