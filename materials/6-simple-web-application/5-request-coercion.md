# 6.5. Request Coercion

Previously we successfully utilized `:body-params` to read values from the request body.
It worked nicely as long as the values were provided.
Yet in the case where the values were missing the results were not as pleasing.

Luckily a bunch of nice Clojure people have prepared solutions for us,
so we don't have to suffer this sorts of issues when building our web service.

As the title might have given away,
solution to our troubles is known as [Coercion](https://github.com/metosin/reitit/blob/master/doc/coercion/coercion.md).

Metosin describes it as follows:

_Coercion is a process of transforming parameters (and responses) from one format into another._
_Reitit separates routing and coercion into two separate steps._

With Reitit there is few different way's to do coercion,
but we will be using what is known as [spec](https://clojure.org/guides/spec).
Spec is a Clojure library that allows us to describe data structures as specs,
and then validate data against those specs.
These specs can be used a sort of predicate functions to describe if the data is valid or not.

That might have been a rather poor explanation and I should rewrite it at some point,
if I come up with better explanation.

Luckily coercion is not actually a very difficult concept,
so it should be clear how it works after we are finished with our next endpoint.

Let's add the following code under our `/subtraction` endpoint:

```clojure
["/division" {:post {:description "Returns x divided by y."
                     :coercion    rcs/coercion
                     :parameters  {:body {:x number?
                                          :y number?}}
                     :handler     (fn [req]
                                    (let [x (-> req :parameters :body :x)
                                          y (-> req :parameters :body :y)]
                                      (response/ok {:quotient (/ x y)})))}}]
```

There is a lot of same going on here,
but few things are a bit different.
The key differences are that we have few new keywords and :body-params seem to be replaced with with something else.

So let's walk through this.
We have added `:coercion` keyword with `rcs/coercion` associated with it.
If you scroll all the way to the top your file,
you notice that rcs is actually `reitit.coercion.spec`.
This specifies to Reitit that we are using spec as our coercion method.
Another new keyword is `:parameters`. Here we specify what sort of parameters this endpoint is expecting.
This is the spec part.
Here we specify that the body of the request should contain a map with keys `:x` & `:y`.
And both of those values should be numbers.
`number?` is standard Clojure predicate function,
which validates if something is a number or not.

You can test it in your REPL.
It works like this:

```clojure
(number? 4)
;=> true
(number? 4.3)
;=> true
(number? 4/3)
;=> true
(number? "John")
;=> false
```

So why are we not using `:body-params` anymore?
Well the thing with coercion in Reitit is that all the coerced parameters will be available at under the key `:parameters`in the request.
So when we are using coercion that is where we should be accessing our params.
The same params are still also available under the `:body-params`.
That information has not disappeared anywhere,
it is just now also available at `:parameters`
You can also notice that our body parameters are placed under the key `:body` in the parameters.
If we would be coercing also path parameters and/or query parameters those would have their dedicated keys as well.

So let's try calling our new endpoint.

```sh
curl --request POST \
  --url http://localhost:3000/math/division \
  --header 'content-type: application/json' \
  --data '{
  "x": 10,
  "y": 3
}'
# {"quotient":3.333333333333333}
```

Proper call works as intended.
But this is not really the part that we were interested about.

Let's try to make a call that is a bit less as what the endpoint is intended for.
What if we would use y as a string instead of an integer?

```sh
curl --request POST \
  --url http://localhost:3000/math/division \
  --header 'content-type: application/json' \
  --data '{
  "x": 10,
  "y": "3"
}'
```

Out put was a bit longer so I formatted it for us:

```json
{
  "spec": "(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$8774/x :spec$8774/y]), :type :map, :leaf? false})",
  "problems": [
    {
      "path": [
        "y"
      ],
      "pred": "clojure.core/number?",
      "val": "3",
      "via": [
        "spec$8774/y"
      ],
      "in": [
        "y"
      ]
    }
  ],
  "type": "reitit.coercion/request-coercion",
  "coercion": "spec",
  "value": {
    "y": "3",
    "x": 10
  },
  "in": [
    "request",
    "body-params"
  ]
}
```

So what do we have here?
Seems like our application returned information that we had a problem with our input.
In the path `"y"` the value was supposed to be a number,
but it was not.

This is quite cool.
I am sure our user will appreciate the feed back,
if they get their calls incorrect.

Try calling the endpoint with missing params.
See what happens.

## Don't divide by zero

Our spec works quite ok,
but it could be a bit better.
We all know that arithmetically considered,
it is not considered cool to divide by zero.
And yet application accepts zero as y.

I am sure we can fix that.
And I doubt it will be not that hard.
All we have to do is to create a tiny bit better spec.

Any predicate is valid for spec.
So we are going to use [`and`](https://clojuredocs.org/clojure.core/and) as our predicate.
It is similar to `&&` used in many other languages.
It takes any number of expressions as arguments,
and then returns either the first falsy one or the last last truthy one.

Like this:

```clojure
(and true "yes" 10)
;=> 10
(and true nil 10)
;=> nil
```

We will wrap `number?` and our `not-zero?` function into `and`,
and use this as a predicate.
The only problem is that Clojure has not `not-zero?` function.
Luckily it does have [`complement`](https://clojuredocs.org/clojure.core/complement).
`complement` turns any predicate into exact opposite version of itself.
This is obviously super handy.

So our solution will look like something like this:

```clojure
["/division" {:post {:description "Returns x divided by y."
   :coercion    rcs/coercion
   :parameters  {:body {:x number?
                        :y (and number? (complement zero?))}}
   :handler     (fn [req]
                  (let [x (-> req :parameters :body :x)
                        y (-> req :parameters :body :y)]
                    (response/ok {:quotient (/ x y)})))}}]
```

Let's replace our old solution with this and try calling our endpoint again with zero.

```sh
curl --request POST \
  --url http://localhost:3000/math/division \
  --header 'content-type: application/json' \
  --data '{
  "x": 10,
  "y": 0
}'
```

Again formatted response should be something like this:

```json
{
  "spec": "(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$8846/x :spec$8846/y]), :type :map, :leaf? false})",
  "problems": [
    {
      "path": [
        "y"
      ],
      "pred": ":clojure.spec.alpha/unknown",
      "val": 0,
      "via": [
        "spec$8846/y"
      ],
      "in": [
        "y"
      ]
    }
  ],
  "type": "reitit.coercion/request-coercion",
  "coercion": "spec",
  "value": {
    "y": 0,
    "x": 10
  },
  "in": [
    "request",
    "body-params"
  ]
}
```

This is already almost perfect.
Except now the predicate says ":clojure.spec.alpha/unknown".
That is kind of a bummer,
since now the users don't really know what was wrong with their request.

We can fix this simply by creating a predicate function with suitable name,
and using that in our spec.
That should be rather straight forward.
Our predicate should look something like this:

```clojure
(defn not-zero-number?
  [x]
  (and (number? x) ((complement zero?) x)))
```

And we make tiny change into our spec.

```clojure
:parameters  {:body {:x number?
                     :y not-zero-number?}}
```

If you now call for the endpoint with zero value,
you can see that the response nicely states that the value failed to be not-zero-number?

```json
// I removed some of the less interesting lines from the response
{
  "path": [
    "y"
  ],
  "pred": "calculator-api.server.server/not-zero-number?",
  "val": 0,
  "via": [
    "spec$9156/y"
  ],
  "in": [
    "y"
  ]
}
```

With this we conclude our section on request coercion.

Next up: [Response Coercion](6-response-coercion.md)