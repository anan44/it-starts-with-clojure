# 5.2 Filtering Data

Thanks to Reddit and its amazing API,
we now have access to large sets of real data.
Processing data is something that Clojure really shines at.

We have already met [`loop`](https://clojuredocs.org/clojure.core/loop),
[`recur`](https://clojuredocs.org/clojure.core/recur),
and [`map`](https://clojuredocs.org/clojure.core/map).

Now we are going to get familiar with other tool for working with sequences.
Meet [`filter`](https://clojuredocs.org/clojure.core/filter)

Filter does exactly what it says.
It filters values from a sequence.
Many languages have similar features,
so this might not be something novel.
Regardless of that Clojure's filter works wonders.

It is a clean and simple solution for remove unwanted items from sequence.
It works somewhat like this:

```clojure
(filter odd? [1 2 3 4 5 6 7 8 9])
;=> (1 3 5 7 9)
```

[`odd?`](https://clojuredocs.org/clojure.core/odd_q) is a clojure function that returns true if the number in question is odd number.
In otherwise it returns false.

So filter takes two arguments: predicate and a collection.
It applies the given predicate function to each value in the collection and removes all values that do not return true.

The out put is always a sequence.
So do not expect to get vector back if you input a vector.

## Predicates

Filter itself are very much straight forward, and there is not much to talk about them. But they do lead us to the concept of [predicates](https://www.tutorialspoint.com/clojure/clojure_predicates.htm).

Predicates are functions that evaluate to true or false.
In Clojure these are commonly the functions with question marks at the end of them.

Clojure core naturally offers many predicates,
but we should not let that limit us,
since it is really easy to write our own predicates as well.

Let's write our own example predicate:

```clojure
(defn larger-than-5?
  [x]
  (> x 5))
```

This function returns true if the number given is larger than 5:

```clojure
(larger-than-5? 6)
;=> true
(larger-than-5? 3)
;=> false
```

After this we can effortlessly use our new predicate with filter:

```clojure
(filter larger-than-5? [1 2 3 4 5 6 7 8 9])
;=> (6 7 8 9)
```

## Real Data, Real Issues

Now we are familiar with `filter`,
but before we get to filtering we'll have to work with some data structures.

Most of our tasks we set to ourselves are related to the posts and the data regarding them.
The API does provide us with this all the data we need, but it is a bit nested,
so we have to use our data skills to access the data effectively.

NEXT ACCESS THE POSTS DATA