# 6.8. Testing Web Applications

We did some testing earlier,
and I secretly hope that you have perhaps written some tests as we go.
Testing web applications is a bit more hustle than writing unit tests for functions,
but luckily not much.

Let's get right to it!

Open up the `test/calculator_api/core_test.clj` file in our project.
As you can see there is already some code provided there by there (as always).
Its a good idea to use this as a kind of template for your tests to come.

Since it is a good practice to write tests into file matching the name of the namespace being tested suffixed by `_test.clj`.
Thus we will be naming our test file `test/calculator_api/server_test.clj`
You can also delete the `test/calculator_api/core_test.clj`, since we won't be using it.

## Preparing for tests

Let's start by writing some necessities to our `server_test.clj`.

```clojure
(ns calculator-api.server.server_test
  (:require [clojure.test :refer :all]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [ring.middleware.params :as params]
            [ring.mock.request :as mock]
            [reitit.coercion.spec :as rcs]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as coercion]
            [calculator-api.server.server :refer [math-routes]]))


(def test-app
  (ring/ring-handler
    (ring/router
      [math-routes]
      {:data {:coercion   rcs/coercion
              :muuntaja   m/instance
              :middleware [params/wrap-params
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-request-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]}})))
```

So here we have some imports, most of them familiar from before.
As a new addition is import from our own code.
We refer `math-routes` from our `calculator-api.server.server` namespace.
And we import `ring.mock.request` as `mock`.
`mock` is used for creating mock requests to our app without actually making http requests.

After imports we define a `test-app`.
We could as well run our tests against our actual app,
but some of the middleware used in the app complicate this a bit.
Thus is it is common to create a dedicated test app for testing routes.
This is supposed to be very similar to the actual app we are running,
but with some alterations to the middleware.
Additionally it is rather usual,
that we include only the routes that we are testing.
In our case we only have `math-routes`,
leaving the `hello-routes` out of scope,
since they are so simple anyways.

Differences between our `test-app` and real `app` is:

`muuntaja/format-middleware` has bee replaced with `muuntaja/format-negotiate-middleware` and `muuntaja/format-request-middleware`.
`format-middleware` is kind of set of middleware, it performs same functionality as `format-negotiate-middleware` and `format-request-middleware`,
as well as `format-response-middleware`.
So in other words, for the tests we want to get rid of `format-response-middleware` functionality.
If we would not do this, mock requests would return java objects,
which would be a drag to operate with.
By doing this change we will be able to operate with clojure data structures,
which is nice of course.

## Testing routes with query-string

With all the preparations complete,
we can now move to writing first tests for this app.

There is many ways to structure your tests,
and it is possible to write infinite test blocks inside each other.
This is just the way I like it,
so in future feel free to experiment with different ways to organize your tests.
Nevertheless,
try to organize them some way.
Just like unorganized code,
unorganized tests are pain in the ass to work with.

Ok let's get to the point:

```clojure
(deftest math-routes
  (testing "GET /math/addition"
    (testing "x and y are added up"
      (let [response (test-app (-> (mock/request :get "/math/addition")
                                   (mock/query-string {:x 5 :y 5})))]
        (is (= 200 (:status response)))
        (is (= {:total 10} (:body response)))))))
```

Just like in [3.4 Testing in Clojure](../3-first-project/4-testing-in-clojure.md) we start with deftest and testing.
For additional clarity we add another testing block inside the first one.
This way the first block shows what method & route combination we are testing,
and the second testing block tells what kind of behavior we are testing.
This is especially useful in real life applications,
where you usually need multiple test scenarios per route.
(This is not the case with our simple math app)

In the actual test we use let statement to declare the response.
The response is formed by calling the test-app with mock request.
We create the mock request by by calling `mock/request` with a method and an uri
Then using thread macro we pass this request to `mock/query-string` to add a query to the request.
`mock/query-string` takes two arguments, request and params.
Params is a map that is then turned into query-string.

Finally we use `is` and `=` to validate that contents of the `response` are as expected.
`response` consists of three parts: `status`, `headers`, `body`.
In our case we are just considered of `status` and `body`.
So we check that `status` is 200 and that `body` is `{:total 10}`

This is all in all rather straight forward.
So let's move to the next endpoint.

## Testing routes with path params

Testing routes with path params is very straight forward.
There really is not much to it.
We will write are mock request to that particular route and check the response.
Remember to write this block inside the `(deftest math-routes)` just after previous test.

```clojure
(testing "GET /math/subtract/:y/from/:x"
    (testing "y is subtracted from x"
      (let [response (test-app (-> (mock/request :get "/math/subtract/10/from/25")))]
        (is (= 200 (:status response)))
        (is (= {:difference 15} (:body response))))))
```

This test is very similar to the previous one,
except we don't need the query string,
since the path itself already contains all the necessities.

## Destructuring response

You might have noticed that we are not actually using response itself for anything,
we are only interested in the contents of it. Namely `body` & `status`.
Some might argument that checking the results has too much happening on one line,
and it could be neater if the `body` & `status` would already have been extracted from the response.
We could use the naive solution to do this, and just move add the extraction into the let statement like
`status (:status response)`, etc.
But there is in fact a nicer way to do this in Clojure.
Depending on your programming background you might have come across destructing in languages like Python or JavaScript.
Clojure also has its own [implementation of destructuring](https://clojure.org/guides/destructuring).

Let's implement destructuring of response to this test:

```clojure
(testing "GET /math/subtract/:y/from/:x"
    (testing "y is subtracted from x"
      (let [{:keys [status body]} (test-app (-> (mock/request :get "/math/subtract/10/from/25")))]
        (is (= 200 status))
        (is (= {:difference 15} body)))))
```

So here we can see that there is something different happening inside the let statement.
Where `response` used to be we how have `{:keys [status body]}`.
So what does this mean?
Well it says that from the map that is returned by the right hands side statement extract the following keys `[status body]`.
Notice that `:`-prefix is not used here,
even though `status` and `body` are keywords.
After this we can refer to status and body without extra hustle.
Also notice that we can no longer refer to response.

Clojure also has other destructuring patterns,
but we won't be covering them here.
In case you are interested you should totally check out the [documentation](https://clojure.org/guides/destructuring).

## Testing routes with json-body

Let's move forward.

So many of our routes contain data in request body.
In order to tests these kind of routes,
we will be using `mock/json-body`.
This works similar to the `mock/query-string`,
but instead of adding the params as query parameters,
they are added as a body.

```clojure
(testing "GET /math/subtraction"
    (testing "y is subtracted from x"
      (let [{:keys [status body]} (test-app (-> (mock/request :post "/math/subtraction")
                                                (mock/json-body {:x 10 :y 5})))]
        (is (= 200 status))
        (is (= {:difference 5} body))))))
```

Nothing unusual is going on here.
Notice that we changed the method to post.

Let's go ahead and add tests for the rest of the routes

```clojure
(testing "GET /math/division"
    (testing "x is divided by y"
      (let [{:keys [status body]} (test-app (-> (mock/request :post "/math/division")
                                               (mock/json-body {:x 15 :y 5})))]
        (is (= 200 status))
        (is (= {:quotient 3} body)))))

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
```

As you can see we also added a test for dividing with zero.

We will also do our destructuring magic on the first test we route.
At this point the tests should look something like this:

```clojure
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
)
```

Don't forget to run your tests from time to time with console command `lein test`.
Everything should be working splendidly.

This should cover up the very basics of testing web applications.
Unfortunately world is not this simple,
and we often have integrations to databases or 3rd party services.
When testing our application we often prefer not to interact with these integrations.
This is where mocking come in.

Next: [Mocking](9-mocking.md)
