# Reducing with our rules

Even though Clojure's core functions are plenty and powerful,
our business logic requirements are often rather specific,
thus we very often end up writing our own functions to use with reduce to achieve desired outcomes.

This will be the case with some parts for our reddit-analyzer as well.
Next we will write the logic for calculating number of posts per author.
On the way we are going to learn few new things,
but by now that should be business as usual already.

So in order to calculate how many posts has each author written,
we need to write a custom function to use with reduce.
Such functions are often referred as _helper functions_.

Our helper function will look something like this:

```clojure
(defn post-count-helper
  [acc x]
  (update acc x (fnil inc 0)))
```

Wow, that probably looks like non-sense to you.
Well no reason to panic,
let's work through it!

`acc` stands for accumulator,
and it is commonly used shorthand that I suggest you will also use.

`x` is the next value iterated by reduce.
In our case it could be also referred as author-name,
but since `acc` and `x` are such a common naming patterns,
that we will use them for now.

## updating value in map

[`update`](https://clojuredocs.org/clojure.core/update) is very heavily used function for updating values in maps.

`update` takes 3 parameters: map, key and function.
Update returns you a new map with changes made.
The original map will remain untouched.
This is a practice in Clojure that you will grow to love.
It is everywhere in the language.

So how will `update` work?
Well it updates the value corresponding the provided key by calling the provided functions with the corresponding value as parameter.

God damn that is an awful explanation.
Just horrible.
Let's hope that code sample will make up for it,
since this is not very difficult concept.

```clojure
(update {:a 1 :b 10 :c 100} :b inc)
;=> {:a 1, :b 11, :c 100}
```

As we can see,
the value at `:b` has been incremented by 1,
which is exactly what `inc` does.

I hope that was good enough clarification.
In case my explanation left you doubtful,
I recommend checking out the official documentation (and comments) for update.
Playing around with update might also be a good idea.

## fnil for nil safety

The other new function we are using here is [`fnil`](https://clojuredocs.org/clojure.core/fnil).

`fnil` gives us power to use default values in case of `nil` values.

So what does this mean?

Well many functions have annoying way of not working if we give them `nil` as an argument.
`inc` for example is one of such functions.

```clojure
(inc nil)
;=> NullPointerException   clojure.lang.Numbers.ops (Numbers.java:1013)
```

(If you are Java programmer this exception might cause cold sweat dripping on your back.)

By utilizing `fnil` we can create a new function,
that uses default values incase a `nil` argument is provided.

```clojure
(def nil-inc (fnil inc 0))

(nil-inc 10)
;=> 11

(nil-inc nil)
;=> 1
```

So here we use `fnil` and `def`to define a new function `nil-inc`.
If we use `nil-inc` to normal numeric values,
it works as normal inc.
Only difference is if argument `nil` is used.
Then the function works just as `inc` would with argument 0.

By no means are we forced to predefine our `fnil` functions like this.
We can just create one on the go.
Like this:

```clojure
((fnil inc 0) 10)
;=> 11

((fnil inc 0) nil)
;=> 1
```

## Back to reducing

Now that we know about fnil and update we can proceed with our helper function.

```clojure
(defn post-count-helper
  [acc x]
  (update acc x (fnil inc 0)))
```

So what happens here,
is that our accumulator (which is a map) is being updated by incrementing the value in the key (which will be the author).
If the value is missing (and we get `nil`),
then default value 0 is being used.

Let's give this function a go with some made up fake data:

```clojure
(reduce post-count-helper {} ["batman" "robin" "superman" "batman"])
;=> {"batman" 2, "robin" 1, "superman" 1}
```

Great!
Our functions seems to be working wonders.

Now that we have the helper function written down,
all that is left is to write the actual post-count function.
This task is easy, since our fake data example has this part mostly covered already.

Whole thing should look something like this:

```clojure
(defn post-count-helper
  [acc x]
  (update acc x (fnil inc 0)))

(defn author-post-count
  [posts]
  (let [authors (map :author posts)]
    (reduce post-count-helper {} authors)))
```

We can call it easily by passing it some posts:

```clojure
(author-post-count (get-posts))
;=> 
;{"mac" 5,
; "k0t0n0" 1,
; "okusername3" 1,
; "dotemacs" 1,
; ...
```

Obviously your return value will be completely different from mine,
since you are going to be running on different time.

This solution is not perfect,
but it works rather well.

With that being said we can move on to the next challenge on our list.
Let's move forward to author total score!

Next: [Partial meetings](5-partial-meetings.md)