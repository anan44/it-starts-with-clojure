# 5.8. Function with No Name

In many parts of this chapter we have been using helper functions.
This is not an unusual pattern,
but in many cases there is better way for achieving this outcome.
In most of the cases instead of defining a new function,
it is desirable to utilize [Anonymous Functions](https://clojurebridge.org/community-docs/docs/clojure/anonymous-function/).

Anonymous Functions are like functions that we create with `defn`,
but they cannot be called elsewhere unless they are explicitly used with other tools that allow such behavior.

A lone Anonymous Function looks something like this:

```clojure
(fn [x] (+ 10 x))
```

The function above takes a single argument and adds 10 to it.
Anonymous Functions can be stored using `def`.
To be exact the following statements are synonymous,
and `defn` is just a helpful macro used to save us some boilerplate.

```clojure
(def add10 (fn [x] (+ 10 x)))

(defn add10
  [x]
  (+ 10 x))
```

Anonymous functions are indeed so popular in Clojure,
that there is actually another way of defining them.

```clojure
(fn [x] (+ 10 x))

#(+ 10 %)
```

Even though this shorthand is very handy,
it is not exactly as capable as the longer version in all the cases.
This is something we will discuss shortly.

Many other programming languages posses similar feature,
which is often referred as lambda functions.
Even though Clojure's Anonymous Functions are not officially referred as lambda functions anywhere in the documentation,
it is not rare for Clojurists to refer them as Lambdas.
Perhaps due to the fact that Anonymous Function is rather mouthful and annoying to write.

Before we move to refactoring our code,
let's familiarize ourselves with Anonymous Functions first with some code.

## Learning Anonymous Functions

As we mentioned above,
Lambdas are used a lot in Clojure.
This is mostly due to the fact that they work so well together with functional iterators,
such as `reduce`, `filter` and `map`.
Anonymous Functions are also used in other ways,
but these are the most common use cases.
When utilizing anonymous function,
we can avoid polluting our code with unnecessary helper functions.
It is also good to note that helper functions are justified in some cases,
especially when the functions are longer.

Let's look at some examples how we can use lambdas together with iterators.

```clojure
(map #(+ 5 %) [10 15 20 29 23])
;=> (15 20 25 34 28)

(map (fn [x] (+ 5 x)) [10 15 20 29 23])
;=> (15 20 25 34 28)

(filter #(> (count %) 3) ["John" "Dan" "Stu" "Rich" "Samantha"])
;=> ("John" "Rich" "Samantha")

(filter (fn [x] (> (count x) 3)) ["John" "Dan" "Stu" "Rich" "Samantha"])
;=> ("John" "Rich" "Samantha")
```

Lambdas can also be used nicely as other helper functions,
which can often clear our code and avoid polluting the namespace with unnecessary functions.

```clojure
(defn remove-old-people
  [people]
  (let [young? (fn [person] (< (:age person) 50))]
    (filter young? people)))

(remove-old-people [{:age 10 :name "Dan"} {:age 55 :name "Janusz"}])
;=> ({:age 10, :name "Dan"})
```

Anonymous Functions can also take multiple arguments:

```clojure
(#(+ %1 %2) 10 15)
;=> 25

((fn [x y] (+ x y)) 10 15)
;=> 25
```

When creating Lambdas that take more than 1 argument,
it is advisable to use longer form instead of the shorthand.
When operating with multiple arguments,
we usually want to give them meaningful names to avoid confusion and chaos.
`fn` form allow us give our arguments any names we see fit,
but `#()` syntax is limited to refer to the arguments based on their order.

Another advantage that `fn` form holds over `#()` syntax is that Anonymous Functions defined with `fn` can be named.
I know it seems like an massive anti-pattern to name an Anonymous Function,
but this is necessary if we aim to achieve recursion.
Function can only call itself,
if it has an identity.
Hope the example below makes this a bit clearer.

```clojure
((fn factorial
   [x]
   (if (<= x 1)
   1 
   (* x (factorial (dec x))))) 10)
;=> 3628800
```

This sort of implementation would not be possible with `#()` syntax,
since lambda defined in such manner cannot refer itself.
In other hand we can chose to give `fn` function a name,
thus it is able to refer to itself

(Don't use this factorial implementation in any real project,
it will not work proper.)

Now we should know enough about Anonymous Functions to use them in refactoring our code.
After we are done with this,
we should have much clearer and smaller code base.

## Refactoring helper functions

We have quite many places where we can use Anonymous Functions to improve our code,
we will refactor each of those parts one by one.

If you have written test for our functionality,
refactoring it should be pleasant and easy.

I always recommend writing tests before refactoring,
to ensure that business logic was not effected by the changes.

### Only good posts

Here our original implementation of `only-good-posts`:

```clojure
(defn good-post?
  [post]
  (> (:score post) 15))

(defn only-good-posts
  [posts]
  (filter good-post? posts))
```

We have created a function `good-post?` to implement mundane business logic,
which we could just as easily implement by utilizing lambda functions.

Let's get rid of the `good-post?` function and refactor `only-good-posts`.

```clojure
(defn only-good-posts
  [posts]
  (filter #(> (:score %) 15) posts))
```

Here we use the `#()` syntax for Anonymous Functions together with filter.
This implementation is clear & simple,
and it leaves little need for explanations.
We can easily conclude that it is ultimately much clearer when using anonymous functions.

### Author post count

We can move forward to refactoring our original implementation of `author-post-count`.
Here is our old implementation:

```clojure
(defn post-count-helper
  [acc x]
  (update acc x (fnil inc 0)))

(defn author-post-count
  [posts]
  (let [authors (map :author posts)]
    (reduce post-count-helper {} authors)))
```

As you can see and remember,
we are using helper function together with reduce.
These kinda helper functions are really common use cases for Anonymous Functions.

```clojure
(defn author-post-count
  [posts]
  (let [authors (map :author posts)]
    (reduce (fn [acc v]
              (update acc v (fnil inc 0)))
            {}
            authors)))
```

Here in the new implementation we have gotten rid of `post-count-helper`,
and replaced it with `fn` Anonymous Function.
We favour using `fn` over `#()` syntax here,
since the lambda in case takes multiple arguments.
`#()` syntax tends to get really confusing when using more than one argument.

We could also argue here,
that the use of let statement to predefine the authors is unnecessary here,
since the code for it is self explanatory.
So let's get rid of that too.

```clojure
(defn author-post-count
  [posts]
  (reduce (fn [acc v]
            (update acc v (fnil inc 0)))
          {}
          (map :author posts)))
```

This solution is short and easy to read.
All the logic required is inside a single function,
with no need to look up other helper functions.
We can easily argue that this is clearly better than our original code.

Our aim here was to learn of the basic tools and techniques for data parsing,
thus we wrote these functions by using rather simple components.
But there is actually a much better way to implement this function by using [`frequencies`](https://clojuredocs.org/clojure.core/frequencies).
Just for fun let's look into that solution,
even when it is a bit outside of our scope.

```clojure
(defn author-post-count-with-frequencies
  [posts]
  (frequencies (map :author posts)))
```

Wow!
That is really short.
Only difficulty comes from the fact that it requires user to be familiar with `frequencies`.
This is clearly better solution.
Clojure has a rather large set of different tools available for data mutations.
It is not rare that you will run into superior tool,
that will make your custom solutions unnecessary,
but it will take some time for you to learn to look for them.
Nevertheless,
it is often good to know that such things do exist.

Let's move on.

### Author total score

Next in line we have our `author-total-score`.
Here is our original solution.

```clojure
(defn total-score-helper
  [acc x]
  (update acc
          (:author x)
          (fnil (partial + (:score x)) 0)))


(defn author-total-score
  [posts]
  (reduce total-score-helper {} posts))
```

It is clearly visible,
that we are using a helper function that can easily be replaced with `fn` lambda.
Let's replace it and see if the solution is better.

```clojure
(defn author-total-score
  [posts]
  (reduce (fn [acc m]
            (update acc (:author m) (fnil (partial + (:score m)) 0)))
          {}
          posts))
```

Here we have gotten rid of the helper,
but the code is a bit messy looking.
We have only 6 lines of code,
but it does feel like it is a bit too hard to follow.
This is most likely due to the one-liner update solution in the middle of the lambda function.
One-liners can feel like a smart idea when writing them,
but trying to read them later is almost always tedious.
Let's see if few line breaks are going to fix the issue.

```clojure
(defn author-total-score
  [posts]
  (reduce (fn [acc m]
            (update acc
                    (:author m)
                    (fnil (partial + (:score m)) 0)))
          {}
          posts))
```

The code might look a bit funny for those who have not used to this kind of indentations,
but it is arguably easier to read.
It is now clear what are the different arguments for the update function.
This also highlights what is the main issue with one-liners in Clojure.
When there is a lot of forms on single line,
it comes harder to see where one argument ends and another starts.
But splitting the logic over several lines of code,
it comes effectively easier to follow what is going on.
And the best part is that line breaks don't cost anything!

_Remember,
having fewer lines of code does not necessarily mean that you have less code!_

That concludes our work with `author-total-score`.
Move on,
shall we!

### Links posted

So our `links-posted` is yet another classic case of helper imitating lambda.
Nothing new here.
This is our our old solution:

```clojure
(defn links-posted-helper
  [acc x]
  (if (empty? (:selftext x))
    (conj acc (:url x))
    acc))

(defn links-posted
  [posts]
  (reduce links-posted-helper [] posts))
```

And as previously,
we will remove the helper function and embed its functionality to `links-posted` with a simple `fn` Anonymous Function.

```clojure
(defn links-posted
  [posts]
  (reduce (fn [acc post]
            (if (empty? (:selftext post))
              (conj acc (:url post))
              acc))
          []
          posts))

```

This solution is clear and simple.
There is not much to explain.
We got rid of unnecessary helper and thats about it,
and because of that it is better solution.
Clean and simple.

That concludes this section on Anonymous Functions (or Lambdas as some people prefer to call them).
But do not worry.
We still have some refactoring left to do before we are truly finished.

Next: [Meet Threading](9-meet-threading-macros.md)
