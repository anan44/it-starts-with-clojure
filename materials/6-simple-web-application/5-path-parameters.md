# 6.5. Path Parameters

This one will be a short one.

We already covered the query params and body params.
That leaves us with third and last type of params.
Path params.

Let's make another endpoint for subtracting.
This one will be using path params.
Wouldn't it be cool if our endpoint would work like a sentence?

```clojure
["/subtract/:y/from/:x" {:get {:description "Returns x subracted by y"
                               :handler (fn [request]
                                          (let [x (-> request :path-params :x Long/parseLong)
                                                y (-> request :path-params :y Long/parseLong)]
                                            (response/ok {:difference (- x y)})))}}]
```

Our code looks rather similar to before,
but this time the path does looks a bit different.
You can see that we have `:y` and `:x` inside the path looking a bit like keywords.
Since we are not using body parameters,
we can use HTTP get.
Furthermore we need to parse numbers again with `Long/parseLong`.
Just like the query parameters,
the path parameters are also strings.

Rest of this is just same old stuff that you are already familiar with.

So let's give our new endpoint a call:

```sh
curl --request GET \
  --url http://localhost:3000/math/subtract/10/from/100 \
  --header 'content-type: application/json'
# {"difference":90}
```

It sees to work like charm.
And the sentence path does look pretty nice.

As promised it was rather short section.
Let's move forward to coercions.

Next: [Request Coercion](6-request-coercion.md)
