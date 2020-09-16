# 6.4. Body Parameters

Previously we learned about query parameters.
They are great.
They work with all HTTP methods and are fairly easy to use.
The problem with query parameters is that they don't work so nicely with more complex data structures.
Furthermore all the characters in query parameters need to be URL-encoded,
which as well might be a pain.

Because of previously listed reasons it is quite common to use body parameters.
This allows us to transfer data in the request body.
One thing to know about body parameters is that they do not work with HTTP GET, HEAD, DELETE or OPTIONS requests,
since those do not have body.

That concludes our pre-lecture this time.
Let's get to the point.

Let's add the following code after our addition endpoint:

```clojure
["/subtraction" {:post {:description "Returns x subtracted by y"
                        :handler (fn [request]
                                   (let [x (-> request :body-params :x)
                                         y (-> request :body-params :y)]
                                     (response/ok {:difference (- x y)})))}}]

```

So what is going on here?
Well this is rather straight forward,
and you might be actually be able to figure this out by yourself.
Perhaps you should try to?

Well I'll explain it all anyway just in case.

Just like before we create a new Route.
We use `"/subtraction"` as the path.
Instead of HTTP GET we use POST this time.
This is done with the `:post` keyword.

In our Route Data we have now two keywords.

`:description` in our case does not do anything,
except it allows us to describe our endpoint.
In theory we could use any key we wish here.
But using `:description` is rather descriptive,
and there is some tools in the Reitit Ecosystem that do rely on this keyword,
so I would advice using it.
Using descriptions is a good practice,
especially when working with other developers.

`:handler` specifies the handler function just like with `/hello` endpoint.
Our handler function takes a request,
from which we get body parameters x and y.
You can see that we use the familiar thread macro `->`.
You might also notice that the x & y are keywords.
For the existence of `:body-params` we can thank our [Muuntaja](https://github.com/metosin/muuntaja) middleware,
which we added into our app earlier.

After fetching x & y all we have to do is to calculate the difference and create the response,
but that should all be familiar to you at this point.

You might have notice that unlike with query parameters,
we did not have to parse the values from strings.
This is because body parameters can actually be numbers.

Ok let's see if our endpoint actually works:

```sh
curl --request POST \
  --url http://localhost:3000/math/subtraction \
  --header 'content-type: application/json' \
  --data '{
    "x": 10,
    "y": 5.5
}'
# {"difference":4.5}%
```

Great! Everything seems to be in order.
You might also have noticed that unlike our addition with query parameters,
this implementation actually works with decimal numbers as well.
This is because we did not parse our numbers into `long`s this time.

If you try calling our endpoint without setting x or y,
you will get some ugly java.lang.NullPointerException accompanied with HTTP status 500.
You might as well try providing some other value types x or y and receive nasty looking exceptions.
That kinda sucks.

In the next part we will see if we could do something about those.

Next up: [Request Coercion](5-path-parameters.md)