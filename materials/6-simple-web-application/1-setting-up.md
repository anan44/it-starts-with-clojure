# 6.1 Setting up

We will begin with adding all dependencies,
which we will be needing in this project.
Since our project is a web application and we dont' want to build every single thing from scratch,
there will be a bit more dependencies than in the previous projects.

As mentioned before,
it is rather common in among clojurists to not rely on a single framework,
but rather build our web application from varying components and dependencies.
Most of the components in Clojure ecosystem can be used freely with each other,
and they are not tied to certain framework or setup.
This kind of approach is great,
since it allows us to build web applications in more flexible manner.
We can with ease utilize what ever components we like and build our stack as we wish.
As great as this is,
it does come with a tiny down side.
Getting into Web Development with Clojure by yourself can be intimidating,
since it might be hard to know what kind of components should you use in yourstack.
Also this forces us to (at least briefly) to deal with things that most of the frameworks do for us.
This might be especially weird if you are coming from Java Spring, Python Django or Ruby on Rails background.

With that being said we can start putting our project together.
We start as usual by creating a new leiningen project.

```bash
lein new app calculator-api
```

## Dependencies

Next we need to add few dependencies to our project,
so open the `project.clj` in your editor.

We will be adding following dependencies to our project.

We are using the latest versions during the time I am writting this.
It is likely that there is newer versions available when you read this.
Feel free to use newer versions,
they should work as just as well,
since Clojure ecosystem is crazy about backwards compitability.
But in case you run into trouble,
try down grading to the versions we are using here.

### [Ring](https://github.com/ring-clojure/ring)

Add following statement into your `:dependencies`

```clojure
[ring "1.8.1"]
```

This is how Ring is described as follows in their own readme:

_Ring is a Clojure web applications library inspired by Python's WSGI and Ruby's Rack._
_By abstracting the details of HTTP into a simple, unified API,_
_Ring allows web applications to be constructed of modular components that can be shared among a variety of applications, web servers, and web frameworks._

In Clojure web development Ring is pretty much de facto standard HTTP server abstraction,
and almost all Clojure web applications rely on it.

Ring allows us to deal with http requests with functions and map data structures.
Combined with different adapters ring takes requests from web servelets and transforms them into standard maps that we can easily work with.
There is also ton of ring compitable middleware available that can easily be plugged in.

### [Reitit](https://metosin.github.io/reitit/)

Add following statement into your `:dependencies`

```clojure
[metosin/reitit "0.4.2"]
```

Metosin is a Finnish company,
which has written a ton of quality packages for Clojure.
If you ever find yourself looking for a package,
it is might worth it to see if they have created a package your needs.

Reitit is a really fast data-driven router for Clojure.
It goes together perfectly with Ring,
and is one the key reasons why web development in Clojure is so much fun.

### [ring-http-response](https://github.com/metosin/ring-http-response)

Add following statement into your `:dependencies`

```clojure
[metosin/ring-http-response "0.9.1"]
```

This cool little package helps us to deal with http status codes in a bit more humane manner.
It is a solid quality of life improvement for web development.

With these three dependencies your project.clj should look something like this:

```clojure
(defproject calculator-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.1"]
                 [metosin/reitit "0.4.2"]
                 [metosin/ring-http-response "0.9.1"]]

  :main ^:skip-aot calculator-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
```

## Boring code aka boilerplate

Since we are not using any framework or template,
we will have to write some boring boilerplate code.
There is plenty of improvements and added functionality that could be put here,
but what we are having should be enough for our needs for this application.

Let's start by creating a new file folder `/server` inside our `/calculator_api`.
Into this folder we will create a new file `server.clj`.
So the full path should be something like `/calculator-api/src/calculator_api/server/server.clj`.

It is a good practice to create a proper foulder structure even if you know your application will be really small.
After all, we never really know what kind of refactoring and future development we will end up doing.

### App boilerplate

Let's start by adding the app boilerplate:

```clojure
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
```

This is the core of the application.
The place where it all comes together.

For this we are going to need to require few dependecies.

Manipulate your `(ns calculator-api.server.server)` so it will look like this:

```clojure
(ns calculator-api.server.server
  (:require [ring.middleware.params :as params]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]))
```

Great!
Let's talk about this a tiny bit.
At this point you don't need to really understand everything here,
but I guess it is still beneficial to have a slightest glue what this is about.

First of all we should note that ring here does not refer directly to ring library,
but instead it refers to reitit.ring.
Thus even though these are thightly connected,
they should not be mixed with each other.
reitit.ring is reitit abstraction that helps us to deal with ring.
It is actually rather rare that we would directly deal with ring,
simply because it is easier to work with higher level abstractions.

app is an outcome of us passing a ring-router, default-handler (optional) and options (optional that we dont use here) into ring-handler.
At this point there is not reason to get too deeply into ring-handler.
It simply returns us a ring handler supporting both syncronious and asyncronious request handling.
In other words it will deal with our http requests.

Default-handler deals with requests that do not match any of our route & method combinations.
So if user requests for unknown route http status 404 is returned.
If method used in request is specified for a route http status 405 is returned.

We are often much more interested in the ring router that we pass to ring-handler.
ring/router creates a router out of raw route data and optional options map.
Here we use options to define the middleware and pass in muuntaja.

Muuntaja is format negotiation, encoding and decoding library.
Here we pass a muuntaja instance.
It checks the request headers and makes sure that our response is the user expects it to be.
If the requests headers request for json response,
then that is what application returns.
If the request headers request for EDN response,
then that is what the application returns.

Finally we pass in bunch of middleware.

**wrap-params** from ring.middleware.params parses the url encoded params from query string and from body.
Without this we would have to perse the params by ourselves,
which is obviously something that we don't want to do since it is boring and error prone.
We will go into more details regarding this in a moment,
but for now you will want to remember to put this into your application.
Almost every application you will write will benefit from this middleware.
Also notice that wrap-params is part of the actual ring library,
and not the reitit.ring as the other things before were.

**format-middleware** from reitit.ring.middleware.muuntaja is the actual middleware for muuntaja.
It cannot be used without the muuntaja instance that we passed before.
This is also a really common middleware to use,
since it is a significant quality of life improvement.
But also something we kind of just put there and can forget about.

Finally we have **coerce-exceptions-middleware, coerce-request-middleware and coerce-response-middleware** from reitit.ring.coercion.
Reitit describes coercion as follows:
_Coercion is a process of transforming parameters (and responses) from one format into another._
I describe coercion as follows:
_Coercion as a process for us to force certain shape/quality for the data coming in and leaving our application._
Since Clojure is dynamically typed language,
we need to be extra certain that the data is as we expect it to be.
If we are not careful,
it is really easy to receive data in unexpected format.
This will obviously cause error,
but it might as well open unnecessary attack vectors to our application that malicious parties can take an advantage of.

**coerce-request-middleware** is for coercing the data from incoming requests.
**coerce-response-middleware** is for coercing the data from our outgoing responses
**coerce-exceptions-middleware** is for dealing with the coercion errors potenttially thrown if the first two throw coercion exception.

### Rest of server boilerplate

Now that we finally finished with the app,
we can move on to the rest of the boilerplate.
Don't worry.
There is not much left.

Add following code to your server.clj.
Preferably att the very bottom of the file.

```clojure
(defonce running-server (atom nil))

(defn start
  []
  (when (nil? @running-server)
    (reset! running-server (jetty/run-jetty #'app {:port  3000
                                                   :join? false})))
  (println "Server running in port 3000"))
```

Here we have few new Clojure core functions.

#### [`defonce`](https://clojuredocs.org/clojure.core/defonce)

`defonce` is just like def,
except it can only be run once per REPL.
If we try to evaluate it again,
it does nothing.
It is a great tool if you want to make sure that certain function is run only once while you are developing.

#### [`when`](https://clojuredocs.org/clojure.core/when)

`when`is just like [ìf`](https://clojuredocs.org/clojure.core/if),
except it does not take a false case.
It runs the the provided code,
if the test evaluates true.
If the test evaluates false,
then the code is not run.

Ok.
Let's get back to buisness.

Here we create an `atom` running-server with `defonce`.
The initial value for the atom is `nil`.
We use `defonce`,
so we don't accidentally overwrite running-server with a new `atom`,
when evaluate code with REPL during the development.

So what does our `start` function do?
Well if the server-running is set to nil,
it runs `jetty/run-jetty` with our app and options,
and passes the output to the atom.
We also print out the "Server running in port 3000".

`jetty/run-jetty` takes a handler and options.
It starts a jetty webserver and serves the supplied handler with the given options.
As handler we give a [var object (short hand #')](https://clojuredocs.org/clojure.core/var) of our app.
(For now I don't want you to worry about var objects, so we will not talk more about them at this moment).
As options we pass a map with a `:portnumber` 3000 `:join?`false.
`:portnumber` defines on which port our application will run.
`:join?` false stops the thread blocking.
Thread blocking is also beyond this exercise.
Nevertheless you should know that unless you explicitely set this false,
you will not be able to use REPL after starting the server.
This obviously sucks so we almost always want to set this as false.

With this being done we have covered the boring boilerplate for our simple web application.
There is plenty of more boilerplate we could add here,
but for the sake of keeping this exercise focused and precise we will not cover more than necerssary.

Next up: [First endpoint](2-first-endpoint.md)

