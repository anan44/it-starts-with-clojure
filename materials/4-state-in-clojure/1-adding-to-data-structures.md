# 1. Adding to data structures

First things first.
Like in previous chapter,
go ahead and create a new Leiningen project using app template.
Name the project as _shopping-list_.
After the project is created,
navigate to the project,
launch REPL and open src/shopping-list/core.clj in your editor.
In case you have problems with this try refreshing your memory in previous chapters.

With the necessities out of the way we can actually get started.
We will play a bit in REPL first to learn few new tricks,
and after wards we will use them in our project.

In the previous project we made an acquaintance with some data structures.
Now we will learn more about them.
As we will progress on this guide,
you will learn that most of our work with Clojure is more or less about playing with those basic data structures.

The structures that we will be using in this chapter are vectors and maps,
both of which you have met previously already.
Let's get to know them a bit better.

So vectors are a structure similar to lists in many other languages.
But unlike the collections of other languages,
Clojure's collections are not necessarily separated by commas,
but they can be if it adds clarity to the code.
In case the commas are being used,
the compiler will ignore them.
In other words,
commas in collections are just syntax sugar.

Previously we have been just seeing collections that we either got from somewhere,
or we created them from some other data.
So far we have not modified any of the data structures we have used.
That is about to change soon.

```clojure
;; vectors can be declared in two ways
(vector 1 2 3)
;=> [1 2 3]
;; or
[1 2 3]
;=> [1 2 3]
;;Both result in the same
(= (vector 1 2 3) [1 2 3])
;=> true
```

There is few ways to add additional elements to the vectors,
and these same methods work with other collections as well.

It is also important to note that these functions DO NOT actually add items to the existing collection.
Clojure's data structures are always [immutable](https://en.wikipedia.org/wiki/Immutable_object) and cannot be changed after their creation.
Instead actually adding items to existing structures,
Clojure returns a new collection with based on the input values.
Depending on your background this might feel odd or slow,
but don't worry about it too much.
You will get used to it quite fast.
And regarding performance: It's fast.

## [conj](https://clojuredocs.org/clojure.core/conj)

`conj` (short for conjoin) adds the element/elements to that part of collection,
where it is most optimal from the performance perspective to do so.
That means the outcomes differ based on the type of collection being used.
With vectors the elements are added to the end,
and with lists the are added to the beginning.
On maps and sets the elements are kinda added where ever,
since they don't guarantee the order of their elements.
Keep this in mind when selecting the collection type that you will be using.
With both lists and vectors it is possible to add elements to other places in the collection as well,
but it is always "cheapest"/fastest performance-wise to add the elements where Clojure naturally does it with `conj`.

```clojure
(conj [1 2 3] 4)
;=> [1 2 3 4]
(conj [1 2 3] 4 5)
;=> [1 2 3 4 5]

(conj '(1 2 3) 4)
;=> '(4 1 2 3)
```

In most cases `conj` should be your goto tool for adding items to collections,
but there is other tools as well that have their use cases.

## [concat](https://clojuredocs.org/clojure.core/concat)

`concat` returns a seq that is the combination of its arguments.
It is rather straight forward to use,
and it can be easily used to add items at beginning of vectors and end of lists,
if that is necessary.

```clojure
(concat [1] [2] [3] [4])
;=> (1 2 3 4)
(concat [1 2 3] [4])
;=> (1 2 3 4)
(concat [1] #{2} '(3) [4])
;=> (1 2 3 4)
```

`concat` is very useful,
but it is good to be aware that it might be a bit slower than `conj`.
Yet in the most cases the speed should not be an issue to your application.

## [cons](https://clojuredocs.org/clojure.core/cons)

`cons` adds an element to the front of the collection and returns it as a seq.
`cons` is rather popular and it definitely has its uses.
But unless you really want to deal with seqs,
AND you want the items added to beginning of a collection then you should consider other options.

```clojure
(cons 1 [2 3 4])
;=> (1 2 3 4)
(seq? (cons 1 [2 3 4]))
;=> true
```

`cons` has its uses but it is a good idea to prefer previous functions when in doubt.

## Adding to shopping list

We will be keeping our shopping list as vector of maps.
The aim will be to make the shopping list object to look something like this:

```clojure
[{:product "milk" :amount "2 liters"} {:product "bread" :amount "1"}]
```

(To keep things simple we wont be using numbers as amount for now)

So how will we add items to the shopping list vector?

```clojure
(conj [] {:product "milk" :amount "2 liters"})
;=> [{:product "milk" :amount "2 liters"}]
```

I would suggest that you will try playing around with adding elements to collections in REPL.
This will help you to get comfortable with the collections modifying them.

Next: [Storing State with Atom](2-storing-state-with-atom.md)
