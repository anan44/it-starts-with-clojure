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