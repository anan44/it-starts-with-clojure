# 6.9. Mocking

Our current web application does not really have any features that require mocking,
but lets make up a scenario:

Our boss asks us to create a experimental MVP feature that that he is looking forward to pitch to the customer.
This thing he is visioning is called BetterMath.
It is an AI powered super feature.
In stead of calculating numbers together like a boring nerd person,
it uses Super Hyper Internet Technologies to predict the number you need.
Boss says he has perfect slogan:

_"Forget the formulas, focus on results!"_

Lucky to us, our boss did not leave us with any kind of specifications how all of this works,
so it is up to us to implement this.

So let's get started:

We come up with perfect algorithm for predicting the number user needs,
which is leaving it all to lady luck.

```clojure
(defn predict
  []
  (rand-int 1000000))
```

But then we realize that one is going to believe that this is based on some Super Hyper technology if the results are calculated instantly.
This is supposed to be advanced science,
so to fake it all we need to slow the calculation down a bit.
So we throw in a `Thread/sleep` to wait a bit.
`Thread/sleep` takes an int and waits that many milliseconds before continuing the execution.

```clojure
(defn predict
  []
  (Thread/sleep 3000)
  (rand-int 1000000))
```

Next we will define new experimental-routes where our predict route is located.

```clojure
(def experimental-routes
  ["/experimental-math"
   ["/predict" {:get {:description "Returns the one number you really need. WARNING: Calculation takes time!"
                      :coercion    rcs/coercion
                      :responses   {200 {:body {:prediction number?}}}
                      :handler     (fn [_]
                                     (let [prediction (predict)]
                                       (response/ok {:prediction prediction})))}}]])
```

Don't forget to include the experimental-routes to the app!

```clojure
(def app
  (ring/ring-handler
    (ring/router
      [hello-routes
       math-routes
       experimental-routes]
      {:data {:muuntaja   m/instance
              :middleware [params/wrap-params
                           muuntaja/format-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]}})
    (ring/create-default-handler)))
```

Next we will write tests for this route,
but how?
This leaves us in a pickle for two reasons.
First, we really don't want to wait three seconds every time this route is tested.
Everyone knows time is money!
Second, we have no clue what number is going to be returned,
so what are supposed to evaluate against?

Luckily mocking exists just for this.

What we are going to do is to mock the predict function.
This way we can still test that the /predict route response look the right way,
but we don't have to wait and we can always get the same number from predict function.

So how is mocking done?

## Meet [with-redefs](https://clojuredocs.org/clojure.core/with-redefs)

Mocking in Clojure is much simpler than in majority of the languages.
All we have to do is to use `with-redefs` function.
`with-redefs`

Let's add the following code to our server_test.clj

```clojure
(deftest experimental
  (testing "GET /experimental-math/predict"
    (testing "prediction is returned"
      (with-redefs
        [calculator-api.server.server/predict (fn [] 42)]
        (let [{:keys [status body]} (test-app (-> (mock/request :get "/experimental-math/predict")))]
          (is (= 200 status))
          (is (= {:prediction 42} body)))))))
```

As you can see the `let` form has been wrapped in `with-redefs` form.
`with-redefs` has similar structure as `let` so it takes vector of bindings and a body.
Bindings are pairs where left side is a function to be mocked (referred with its full name including the namespace),
and the right side is the new function we want to temporarily redefine it with.
If any code inside of `with-redefs` form calls our mocked function,
our mock functions is called instead.
Just as with let,
we can provide as many bindings as we wish within one `with-redefs` form.

Here we have decided to redefine `calculator-api.server.server/predict` to be function that always returns 42.
This new mock does not include any kind of waiting or sleeping,
so our tests will run fast and smooth.

Rest of the test is just as our previous tests,
so nothing new there.

Try running the new test with and without the mock,
so you can see the difference.

## Mocking made easy with [constantly](https://clojuredocs.org/clojure.core/constantly)

We can make a tiny improvement to our previous implementation by utilizing Clojure function `constantly`.
`constantly` is a funny function.
It returns a new function that always regardless of the parameters provided will return the given value.
This new function can be called with any number of arguments,
but the result is always the same.

Give it a try:

Run following things in your REPL:

```clojure
(def constant
  (constantly "Cute kittens"))

(constant 1 {:map 6} "Mr. President")
;=> "Cute kittens"
```

So let's replace our `(fn [] 42)` with `(constantly 42)`:

```clojure
(deftest experimental
  (testing "GET /experimental-math/predict"
    (testing "prediction is returned"
      (with-redefs
        [calculator-api.server.server/predict (constantly 42)]
        (let [{:keys [status body]} (test-app (-> (mock/request :get "/experimental-math/predict")))]
          (is (= 200 status))
          (is (= {:prediction 42} body)))))))
```

You have now learned all there is to mocking with Clojure.
It is really all that simple.
You can use `with-redefs` to mock pretty much anything in Clojure.

There is more advanced features you can use with `with-redefs`,
but we won't cover them here.

In case you are curious and want to do even more advanced mocking,
I advice you to familiarize yourself with [tortue/spy](https://cljdoc.org/d/tortue/spy).
`tortue/spy` offers some great functionality that you will definitely find useful in your projects.

This finalizes our journey to web applications with Clojure.
You have now familiarized yourself with all the basics required for starting to work with real life Clojure applications.

Next: [Final Words](../final-words.md)
