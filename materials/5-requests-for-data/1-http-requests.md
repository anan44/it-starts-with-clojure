# 5.1 HTTP Request

As usual start by initializing a new project with Leiningen:

```bash
lein new app reddit-analyser
```

Nothing new here.
Let's move forward.

As usual we are going to have a ton of new cool stuff coming up in this chapter.
In fact we are having that many new cool things that I had a slight problem deciding what to start with.
After some pondering I thought it would be cool to start with [HTTP requests](https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol).

For performing performing http requests with Clojure we are going to be using an external module.
This module is [dakrone/clj-http](https://github.com/dakrone/clj-http).

Before we can actually get started with the clj-http itself,
we have to briefly cover another topic.

## Clojars

So far we have only used dependencies from within Clojure's source code,
but this time we are using an external dependence.
For this we have to learn how to deal with external dependencies in our Leiningen project.

We are also going learn about [Clojars](https://clojars.org/),
Clojure's repository for open source libraries.
In other words Clojars is the place that you want to be looking for dependencies for your project.
It is a great place to start,
when you suspect there might exist a library for the code problem you are having.

Using Clojars is dead easy.
Just write a search word to the search field and see what the repository has to offer.
You can browser through options from the easy UI.
Many projects have links to their git repository where you can see their source code.
Additionally you have the information regarding how many times the given dependency has been downloaded.

Another very basic information is the dependency code that you can directly copy to your project,
so you can effortlessly use a given dependency.

Since we are using Leiningen we will be using the snippet from labeled as **Leiningen/Boot**.
As you can see there are also other options for dependency handling, namely Gradle and Maven.
These are great tools for Java or Kotlin,
but with Clojure I would advice you to stay away from them.

## 3rd Party Dependencies

Now that we know enough about Clojars we can move forward.
Next we will search for clj-http from Clojars.
This should show us [following page](https://clojars.org/http-kit).

Depending when you are doing this you should see something like this as Leiningen/Boot snippet:

```clojure
[clj-http "3.10.0"]
```

Clojure ecosystem is rather heavily leaning to backwards compatibility,
so there is a very good chance that our codes are going to work with newer versions as well.
But for some reason you will run into weird bugs that you cannot explain,
try using the exact same versions as I am using here.

In order to use this library,
we'll have to add this snippet into our project's dependencies in project.clj file.

Currently your project.clj should look something like this

```clojure
(defproject reddit-analyser "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot reddit-analyser.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
```

This is how Leiningen projects are defined.
There is information regarding the main namespace and other things required to build and run the project.
We are not going to dive into details of all of this,
because out of our scope for now.

In order to use clj-http in our project we'll have to add it to the dependencies vector.

Like this:

```clojure
(defproject reddit-analyser "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.10.0"]]
  :main ^:skip-aot reddit-analyser.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
```

But that is not all.
As we can see from the [clj-http documentation](https://github.com/dakrone/clj-http#optional-dependencies),
clj-http can take additional dependencies.
These are the things that modify its behavior.
One of these is [cheshire](https://github.com/dakrone/cheshire),
which enables clj-http to communicate in JSON instead of EDN.
This is kinda behavior we almost always want,
let's add that into the dependencies as well.
It is a good habit to add this,
since we really rarely wish to actually use EDN in our HTTP queries.
Forgetting to use it will cause weird issues.

So lets add [cheshire](https://github.com/dakrone/cheshire) to our dependencies.

```clojure
(defproject reddit-analyser "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.9.0"]
                 [clj-http "3.10.0"]]
  :main ^:skip-aot reddit-analyser.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
```

Cheshire is defacto the standard for JSON encoding and decoding in Clojure.
You should think it as go to tool if and when you need such functionalities.

In order to make the dependencies work,
it might be necessary for you to restart your REPL.

With these dependencies being sorted out,
we can move to the actual topic of this section: HTTP requests.

## Making HTTP request with clj-http

Let's start by opening our reddit_analyser.core.clj file,
and require clause to our [`ns`](https://clojuredocs.org/clojure.core/ns) statement:

```clojure
(ns reddit-analyser.core
  (:require [clj-http.client :as client])
  (:gen-class))
```

(Remember to evaluate this statement in your REPL before using the dependencies)

Here we are using :as keyword to give alias `client` for `clj-http.client`.
Please note that after using :as the dependency cannot be referred with its default name.

Basic HTTP queries with clj-http are rather straight forward:

```clojure
(client/get "http://www.example.com/")
;=>:cached nil,
;:request-time 332,
;:repeatable? false,
;:protocol-version {:name "HTTP", :major 1, :minor 1},
;:streaming? true,
;:http-client #object[org.apache.http.impl.client.InternalHttpClient
;                     0x7e7180e9
;                     "org.apache.http.impl.client.InternalHttpClient@7e7180e9"],
;:chunked? false,
;:reason-phrase "OK",
;:headers {"X-Cache" "HIT",
;          "Server" "ECS (bsa/EB24)",
;          "Content-Type" "text/html; charset=UTF-8",
;          "Connection" "close",
;          "Accept-Ranges" "bytes",
;          "Expires" "Sun, 03 Nov 2019 20:35:50 GMT",
;          "Etag" "\"3147526947\"",
;          "Date" "Sun, 27 Oct 2019 20:35:50 GMT",
;          "Vary" "Accept-Encoding",
;          "Last-Modified" "Thu, 17 Oct 2019 07:18:26 GMT",
;          "Cache-Control" "max-age=604800"},
;:orig-content-encoding nil,
;:status 200,
;...
```

You can see that we we are getting a super long map as a return value.
This map has ton of data for us.
Much of it is regarding the request itself,
but it can be useful.
Usually the part we are interested in is the body of the response.
This can logically be found from behind the keyword `:body`
In this particular case we are making a get request for normal HTML website,
so our body is a string of HTML code.