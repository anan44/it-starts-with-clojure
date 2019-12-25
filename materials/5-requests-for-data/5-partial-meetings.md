# 5.5. Partial meetings

In order to perform the task of calculating the total score of each author,
we are going to define a helper function to utilize it with `reduce`.

We'll use [`partial`](https://clojuredocs.org/clojure.core/partial) while defining our helper function.
`partial` is a handy tool for defining new functions based on existing ones.
It is especially commonly used together with iterators such as `map` or `filter`.
Like `fnil`,
`partial` returns a new functions that is based on the function given as an argument.

So what does `partial` do?
It is fairly simple function.
`partial` takes a function and n number of arguments.
It then returns a function where these arguments are hard coded,
but remaining arguments are still to be provided by user when invoking the new function.

Let's look at this in action:

```clojure
((partial + 10) 5)
;=> 15

((partial str "Hello, ") "World")
;=> "Hello, World"
```

Number of arguments has to be less or equal to what the provided function takes,
or it will naturally result in an exception.

So why is partial used?
We well we often have cases where we would like to always use the same parameter or parameters,
or we would like to use a function as a parameter to another function,
which has limitations regarding the number of arguments the provided function should accept.
Such is the case here.

Our helper function looks something like this:

```clojure
(defn total-score-helper
  [acc x]
  (update acc (:author x) (fnil (partial + (:score x)) 0)))
```

That is a quite a lot happening on a single line of code.
Let's try breaking it up,
so it is easier to focus on one argument at a time.
(This is often a handy trick when slapped in the face with long one liner functions)

```clojure
(defn total-score-helper
  [acc x]
  (update acc
          (:author x)
          (fnil (partial + (:score x)) 0)))
```

So here we again have `acc` (accumulator) and `x` (a single post),
nothing new there.
Then we are again calling `update` on `acc` as we did before,
so still clear and simple.
As key argument we are providing what ever is behind `:author` keyword in the post,
which is obviously the author of the post.

Then comes the difficult part.

When reading LISP such as Clojure,
it is good to start from the inner most parentheses.
In (:score x) we get the score on the post given.
We then use that in partial together with `+` function.
So we are creating a function,
which with single argument will return score plus the score of this post.
Then that function we are passing to `fnil` with default 0.

So essentially the function takes a number and then adds score of post to it.
This is then the function we use together with `update`.
All of this might seem overly complicated for now for,
but it rather safe way to iterate and sum things together in comparison to traditional for and while loops.
Furthermore,
it only feels difficult until you get used to this kind of functional thinking.

Next we should test try our helper function.

```clojure
(total-score-helper {"Rich" 15 "Bob" 10} {:author "Steve" :score 2})
;=> {"Rich" 15, "Bob" 10, "Steve" 2}

(total-score-helper {"Rich" 15 "Bob" 10} {:author "Bob" :score 2})
;=> {"Rich" 15, "Bob" 12}
```

Great! The function seems to be working wonders.
Now we only have to tie it together with `reduce`:

```clojure
(defn author-total-score
  [posts]
  (reduce total-score-helper {} posts))

(author-total-score (get-posts2))
;=> {"mac" 192,
; "k0t0n0" 36,
; "okusername3" 16,
; "dotemacs" 27,
; "yogthos" 375,
; "tscrowley" 15,
; "childofsol" 30,
; ...
```

As usual your numbers will probably differ from mine.

Next we will work on filtering down to all the posts that have links.

Next: [Give me links](6-give-me-links.md)
