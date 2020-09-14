# 6.3. Query Parameters

Now that we have made our first route,
we can try to spice things up with some basic Reitit & Ring concepts.

In this section we will create a math-routes that will perform basic arithmetic operations.
Application like this has close to no real life applications,
but it is a good way to work through the basics of Clojure Web Development.

In a real world scenario you would like create similar interfaces for your endpoints.
When the endpoints follow logical pattern,
it is easier for the users to use it.
In our case we will start by using different styles of interfaces.
This would not necessarily be a good practice,
but we will suffer it for the sake of learning.

Let's try to keep this one short and get started right away.

We will begin by by defining math-routes.

```clojure
(def math-routes
  ["/math"
   []])
```

Unlike last time we don't follow up the Route Path with a map,
but instead we use a vector.
For now the vector is empty,
but this will no longer be the case after a moment.
This kind of data structure allows us to group several routes under single route.

This way we can represent our api like this:

```clojure
["/person"
  ["/age" {...}]
  ["/name" {...}
  ["/address" {...}]]]
```

Instead of this:

```clojure
  ["person/age" {...}]
  ["person/name" {...}]
  ["person/address" {...}]
```

This sort of grouping can significantly clarify our code.
This is especially the case when the paths are longer and there is more of them.
In [Retit documentation](https://cljdoc.org/d/metosin/reitit/0.5.5/doc/introduction) this referred as Nested Route Data.
Nested Routes would also allow us to group multiple routes,
so same middleware is used with them.
This too can be really handy.
(If you don't know what middleware is,
don't worry about it.
We will learn about it later on this course.)

Ok! We got a bit side tracked there.
Let us refocus and get back to coding.

Our skeleton `math-routes` is not going to do anything in its current state.
So lets add some meat to those bones.
What we want to do is to create basic addition endpoint.
This addition endpoint should ready two query parameters x and y.
Then it should return the total of those numbers.

Ok lets give it a go:

```clojure
(def math-routes-wip
  ["/math"
   ["/addition" {:get (fn [request]
                        (let [params (:query-params request)
                              x (Long/parseLong (get params "x"))
                              y (Long/parseLong (get params "y"))]
                          (response/ok {:total (+ x y)})))}]])

```

Furthermore,
we also have to add our math-routes into our router:

```clojure
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
```

After making the changes you might want to reload the namespace,
stop the router and start it all over again.
After that we should be able to call our endpoint.

```sh
curl --request GET --url 'http://localhost:3000/math/addition?x=10&y=5'
# {"total": 15}
```

Great it works!

In case you are not familiar with the query parameters,
those are the following part in our HTTP request: `?x=10&y=5`

So what is going on with our code?
Let's walk through it real quick.
There is a lot of familiar stuff there.
Route Path`"/addition"`,
HTTP method `:get`,
and handler `fn` we already met in the previous section.
Major differences here are inside the handler.
This time we have named our single parameter `request`.
Some people call this `req` but we won't be doing that since we like to be precise.

Any query parameters used by the user can be found from the request.
So the first thing we do inside let statement is to get those parameters with`(:query-params request)`.
After this we get both the params `x` and `y`.
All query parameters are always strings,
so we need to parse them into numbers.
We can achieve this by using `Long/parseLong`.
After the `x` and `y` have been successfully parsed into Longs,
we can create the ok response with total by using `response/ok` and standard maps and `+` function.

Most of this should be familiar to you from the prior chapters.
New stuff should be `request` & `:query-params` and perhaps `Long/parseLong`.
`Long/parseLong` is very similar `Long/valueOf`,
so I wanted to show it you. 
The difference is in the other return Java's `long` and the other Java's `Long`.
From Clojure perspective this does not make much of a difference.

`request` is an interesting creature.
It contains a lot of information regarding the request made by the user,
and it can be further enriched with middleware.
All the data in `request` is access with basic data structure operations that we are already familiar with.

Some of you might have been proactive and tried to calling this endpoint with decimal values.
That will not work,
since decimals cannot be parsed into `long`.
We will not care about this now,
but we might fix it later.

That completes the brief introduction into query parameters in Ring & Reitit.

Next up: [Body Parameters](4-body-parameters.md)
