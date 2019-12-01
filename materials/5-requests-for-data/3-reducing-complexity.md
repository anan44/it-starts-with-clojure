# 5.3. Reducing Complexity

Well I am not completely sure how I feel about this title but let's go with it,
since I did not come up with anything better on the spot.A

So previously we have learned about working through collections with `loop`/`recur`, `map` and `filter`.
We could without lying too much state that you could get pretty far with these alone.
Nevertheless, this merry band of functions is still missing one partner.

A certain course I studied when I was getting started with Clojure put it this way:
[_One Function to rule the all_](http://iloveponies.github.io/120-hour-epic-sax-marathon/one-function-to-rule-them-all.html).

And this is not very far from the truth when we are talking about functional programming.

Without further ado!
Our next quest:

## [Reduce](https://clojuredocs.org/clojure.core/reduce)

In addition to the recursion,
`reduce` is one of the primary ways to iterate in Functional Programming.
And thus it will become your dear friend when goofing around with Clojure.

`reduce` can feel both soothingly simple and annoying complicated,
and oddly even both at once.
It is a powerful tool,
but I am not going to lie to you.
If you have not used `reduce` in your previous coding adventures,
it might take a moment to get comfortable with `reduce`.

`reduce` is a tool that can be found in one form or another from most of the programming languages.
At least ones that have [first class functions](https://en.wikipedia.org/wiki/First-class_function).

Without chanting more about radiance of `reduce`,
let's take a look at it:

```clojure
(reduce + 0 [1 2 3 4 5])
;=> 15
```

So what does it do?
Reduce takes 3 arguments.
Those being function, value, and collection,
or as official documentation puts it:
_f, val, coll_.

Reduce cannot take just any function,
instead it has to be a function that takes two(2) arguments.
Value is something like a starting value.
It is a bit unnecessarily complex to explain,
so I think it is better to just demonstrate it in a moment.
Collection is the simply the collection we wish to iterate through.

So what does `reduce` do with these parameters?
In short it works in iterations repeatedly calling the function again and again with different arguments.
On the first iteration the function calls the function by providing value and first item in the collection as arguments.
The return value of the function is then stored into what we refer as "accumulator",
which again is used as argument together with second item in the collection.
The patter repeats until all the values in the collection has been iterated through.
After no items are left to be iterated,
the accumulator value is returned.

I am personally not very happy with this explanation,
but nether am I with almost any explanation for reduce that I have seen.
Instead I believe it is much better explained with code examples.

Let's have above example written out in kinda step by step representation:

```clojure
(reduce + 0 [1 2 3 4 5])
(reduce + 1 [2 3 4 5])
(reduce + 3 [3 4 5])
(reduce + 6 [4 5])
(reduce + 10 [5])
(reduce + 15 [])
;=> 15
```

Value argument can be considered as initial value for accumulator,
because that is quite accurately what it really is.

Value itself has little limitations,
and it can be either numbers, strings, vectors, maps, sets, or combinations of those.
Map of sets?
Why not.
Vector of maps?
If you wish so.

Here is an example for turning a vector into set with `reduce`:

```clojure
(reduce conj #{} [:a :b :c])
;=> #{:c :b :a}
```

Here we split a string into vector of characters utilizing `reduce`:

```clojure
(reduce conj [] "Rich Hickey")
;=> => [\R \i \c \h \space \H \i \c \k \e \y]
```

([Rich Hickey](https://github.com/richhickey) is the [BDFL](https://en.wikipedia.org/wiki/Benevolent_dictator_for_life) for Clojure)

`reduce` can actually be purposed to perform the actions of map or filter,
even though I would not recommend you to over use it like that in the long run.
But it can be a fun way to get comfortable with reduce.

Another thing about reduce is that the value argument in non-obligatory.
In some cases we could leave it out and get the same result.

```clojure
(reduce + [1 2 3 4 5])
;=> 15
```

If value argument is not provided first iteration of reduce will use first and second items on the collection as arguments.
We mostly use the value argument we need to define the type of the output for our solution.

For example this works:

```clojure
(reduce conj #{} [:a :b :c :a :a :b])
;=> #{:c :b a}
```

and this doesn't:

```clojure
(reduce conj [:a :b :c :a :a :b])
;=> CompilerException...
```

This is simply because we cannot conjoin :b to :a.

```clojure
(conj :a :b)
;=> CompilerException...
```

## Calculating average

If we look at our list of requirements,
next one in line is calculating the average score for all the posts.
In calculating this we will be utilizing `reduce`.

To calculate the average score we need two things:
The total number of posts and total score of all the posts.
We will write the function in manner that it can used for any number of posts,
not only for predefined number.

Our solution is rather straight forward:

```clojure
(defn average-score
  [posts]
  (let [post-count (count posts)
        total-score (reduce + (map :score posts))]
    (/ total-score post-count)))
```

Here we use `let` to create two variables in lexical scope.
One being post-count and one being total-score.
After this we return the total-score divided post-count.

`post-count` is rather straight forward,
so won't be going through that.

`total-score` instead is a bit more juicy.
Let's see what that is about:
In total-score we first use `map` to get score for each post by utilizing the `:score` keyword.
(Remember, we can use key words as functions.)
After this we use combination of `reduce` and `+` to get total of these scores.

Let's see how this works:

```clojure
(average-score (get-posts))
;=> 698/25
```

(Your result probably differs from mine,
since we are doing this on different times)

As we can see,
the average is displayed as [ratio](https://clojure.org/reference/data_structures#_ratio) instead of a decimal number.
By default Clojure always displays divisions as ratios.
Nevertheless,
this ratio is rather nonsense,
since we cannot really tell much by just looking at it.
In our case it would be much nicer just to have a plain old decimal number.
Thankfully we can achieve this rather effortlessly.

Let's make a small adjustment to our code:

```clojure
(defn average-score
  [posts]
  (let [post-count (count posts)
        total-score (reduce + (map :score posts))]
    (float (/ total-score post-count))))

(average-score (get-posts))
;=> 27.92
```

By running our return value through [`float`](https://clojuredocs.org/clojure.core/float) function,
it will be presented as decimal number instead of ratio.

Small note regarding reddit api:
I have noticed that the scores returned by reddit are not very stable,
they constantly go up and down every time you call the API.
So don't be alarmed if you get different result by each call.

## Reducing with our own rules

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

### updating value in map

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

### fnil for nil safety

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

### Back to reducing

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
Let's get author total score!

### Partial meetings


