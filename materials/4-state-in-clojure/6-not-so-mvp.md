# 4.6 Not so MVP

We previously made a working MVP of the application.
It can currently build a shopping list to the memory,
and save it on a file.
Nevertheless our code is far from optimal.
We have some unnecessary repetition in the code,
and we currently save the shopping list in a format known as [EDN](https://github.com/edn-format/edn).
I won't go to details on EDN, but it stands for Extended Data Notation.
It is like Clojure's version of JSON,
and it is very handy since it allows us to dump and read data without much parsing.
Regardless of how awesome EDN is,
it is not very human friendly.
Sure you can read it,
but it ain't very pretty.

## Saving in human friendly format

We would prefer our shopping list to be saved in more human readable format.

Something like this perhaps:

```text
Milk * 3
Bread * 2
Eggs * 12
```

In order to achieve this,
we need to process our vector a bit.

Let's make a function for turning our shoppings into a vector of strings.

For this we will use two functions:

```clojure
(defn shopping->str
  [x]
  (str (:product x) " * " (:amount x) "\n"))
```

`shopping->str` takes a single shopping map and turns it to a string.
(Using this kind of arrow style definition is very popular in Clojure)
Here we use keywords as functions to retrieve the matching element from the map.
This also a very frequent practice in Clojure.

### Retrieving data from maps

Let's explore it a bit before we get back to our solution.

```clojure
(:product {:product "Milk" :amount "3"})
;=> "Milk"

({:product "Milk" :amount "3"} :product)
;=> "Milk"

(get {:product "Milk" :amount "3"} :product)
;=> "Milk"
```

So a keyword can be used as a function that takes a parameter of a map,
which will result in an output of the corresponding value from the map.

Map can also be used as a function that takes a parameter of keyword,
which will result in an output of the corresponding value from the map.

Finally we have also explicit `get` function that takes parameters map and keyword,
which will result in an output of the corresponding value.

But that's not all.
We are not forced to use keywords as keys in maps.
It just is very common due the practicality it provides.

```clojure
({"product" "Milk" "amount" "3"} "product")
;=> "Milk"

(get {"product" "Milk" "amount" "3"} "product")
;=> "Milk"

("product" {"product" "Milk" "amount" "3"})
;=> Syntax error (ClassCastException) compiling at...
;class java.lang.String cannot be cast to class clojure.lang.IFn...
```

So a string cannot be used as a function,
which is why we get an error that it cannot be cast to IFn (Interface Function).

Let's explore few more cases while we are at it.

```clojure
(:price {:product "Milk" :amount "3"})
;=> nil

(:price {:product "Milk" :amount "3" :price nil})
;=> nil
```

You probably have some prior experience in programming,
so I guess you might know why this is troublesome.
There is no way for us to know,
if we actually had a value `:price` that just happened to be `nil`,
or if we just completely lacked the value referred as `:pric``.

### [get](https://clojuredocs.org/clojure.core/get)

As a solution to issue,
get function takes an optional 3rd parameter that will be returned,
if the key is not present in the given map.

```clojure
(get {:product "Milk" :amount "3"} :price "NotFound")
;=> "NotFound"

(get {:product "Milk" :amount "3" :price nil} :price "NotFound")
;=> nil
```

### Back to the topic

Now that we have that part of maps covered,
we can get back to our solution:

```clojure
(defn shopping->str
  [x]
  (str (:product x) " * " (:amount x)))
```

So here we are retrieving values `:product` and `:amount` from x,
after which we proceed to build a single string as an output.

The next step for us is to call this function on each item on our shoppings vector.

We could use the loop that we just learned of:

```clojure
(defn shopping-printable
  [shoppings]
  (clojure.string/join
    "\n"
    (loop [a-vec shoppings
           out []]
      (if (empty? a-vec)
        out
        (recur (rest a-vec)
               (conj out (shopping->str (first a-vec))))))))

```

But not only is that a quite a handful of a function,
it is also non-optimal performance-wise.
This is due the fact I mentioned before:
loop is not [lazy](http://clojure-doc.org/articles/language/laziness.html).

Thus I am not even going to explain to you how this monster of a function works.
Instead we are going to jump directly to a proper version:

As mentioned before,
Clojure's `loop` is a flexible tool,
which is able to solve many problems.
But instead of over relying on it,
you should explore other options.
It is often the case that other tools are able to solve your problems more effectively.
Both clarity- and performance-wise,
as is the case now.

The far better solution looks like this:

```clojure
(defn shopping-printable
  [shoppings]
  (clojure.string/join
    "\n"
    (map shopping->str shoppings)))
```

As you can quickly see,
it is far shorter and there is much less happening in this solution.
But additionally it provides us with the benefits of the laziness,
which we get kind of "for free" for using the [`map`](https://clojuredocs.org/clojure.core/map) functions.

So what does actually happen here?
Well our function takes a single parameter shoppings,
which we transform with `map` function and `shopping->str` that we defined before.

The we use the [`join`](https://clojuredocs.org/clojure.core/map) function from the clojure.string namespace,
which takes 2 or 3 parameters.
Here we have the 3 parameter option,
where we provide the separator for the function.
With this option it joins each item on a collection to single string and writes the separator in the between each item.

Thus as an output for this pretty function we get a single string containing all of our individual shoppings in single string separated with a line brake.

So with an ability to create printable shopping,
we can move forward.

For clarity let's wrap all this saving stuff into single function.
That way it is much more fun to handle it in the main function.

```clojure
(defn save-shopping-list
  [file shoppings]
  (spit file (shopping-printable shoppings)))

```

All that is left to improve the quality of our output document,
is to include our new logic.

```clojure
(defn -main
  [& args]
  (loop [choice (prompt "Enter a number:\n1. Add product\n2. Save shopping list")]
    (if (= choice "1")
      (do (add-product-to-shoppings)
          (recur (prompt "Enter a number:\n1. Add product\n2. Save shopping list")))
      (if (= choice "2")
        (do (save-shopping-list "./things-to-buy.txt" @shoppings) ;;We changed this part
            (println "Shopping list saved"))
        (do (println "Invalid choice!!! Try again")
            (recur (prompt "Enter a number:\n1. Add product\n2. Save shopping list")))))))
```

Remember to give it a try to see that your code works as intended.

## Reformating the Code

So now that we have reached the level of functionality that we set as our target in the beginning,
it is time to do some refactoring.
You should always refactor your code to make it better,
and clear out the nonsense that all of us accidentally create from time to time.

In our case there is few improvements that we could strive for.

### Reducing repetition with def

We can see that we are using the same string prompting user for decision in multiple places.
It seems a bit unnecessary to have it written like that multiple times.
After all we are fans of [DRY principle](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself).

So let's define options with `def`:

```clojure
(def options "Enter a number:\n1. Add product\n2. Save shopping list")
```

It is common to save all the defs like this at the top of your file after the namespace declaration
This makes code often more readable,
and I suggest you do the same.
It is generally a good idea to have all the defs in one place.
According to [style guide](https://guide.clojure.style/#empty-lines-between-top-level-forms),
there is no need to separate them with empty lines.

We will also modify our main function to utilize this options variable.

```clojure
(defn -main
  [& args]
  (loop [choice (prompt options)]
    (if (= choice "1")
      (do (add-product-to-shoppings)
          (recur (prompt options)))
      (if (= choice "2")
        (do (save-shopping-list "./things-to-buy.txt" @shoppings)
            (println "Shopping list saved"))
        (do (println "Invalid choice!!! Try again")
            (recur (prompt options)))))))
```

That is already much better,
but let's also save the output file as a value with `def`.

```clojure
(def output-file "./things-to-buy.txt")
```

And the necessary change to main function:

```clojure
(defn -main
  [& args]
  (loop [choice (prompt options)]
    (if (= choice "1")
      (do (add-product-to-shoppings)
          (recur (prompt options)))
      (if (= choice "2")
        (do (save-shopping-list output-file @shoppings)
            (println "Shopping list saved"))
        (do (println "Invalid choice!!! Try again")
            (recur (prompt options)))))))
```

This is already much better.
The code is now less bloated and easier to read.
That's great, right?

## a [case](https://clojuredocs.org/clojure.core/case) for less ifs

In Clojure (and other programming as well) nested `if` statements are often frowned upon.
This is because they often cause bugs and are a pain in the ass to debug.

As a solution to that we have the [`case`](https://clojuredocs.org/clojure.core/case) macro

`case` takes an expression and n number of clauses.
When run the case will execute the clause that matches the expression.

```clojure
(case "3"
  "1" (str "not" "this" "one")
  "3" (str "this" " one")
  "6" (str "not " "here either"))
;=> "this one"
```

If no matching clause is provided,
an error is thrown:

```clojure
(case "5"
  "1" (str "not" "this" "one")
  "3" (str "this" " one")
  "6" (str "not " "here either"))
;=> Syntax error (IllegalArgumentException) compiling at...
No matching clause: 5
```

To avoid this we can provide a default clause:

```clojure
(case "5"
  "1" (str "not" "this" "one")
  "3" (str "this" " one")
  "6" (str "not " "here either")
  "I am the default")
;=> "I am the default"
```

Note that default can be pretty much anything.

You should also know that Clojure also has a [`cond`](https://clojuredocs.org/clojure.core/cond) macro,
which is very similar to `case`.
In case you are interested,
please see the documentation since we won't go through `cond` just now (we will do so later though).

So it seems obvious how we could improve our code by utilizing `case`,
doesn't it?

```clojure
(defn -main2
  [& args]
  (loop [choice (prompt options)]
    (case choice
      "1" (do (add-product-to-shoppings)
              (recur (prompt options)))
      "2" (do (save-shopping-list output-file @shoppings)
              (println "Shopping list saved"))
      (do (println "Invalid choice!!! Try again")
          (recur (prompt options))))))
```

So here we replaced the `if` statements with cases "1" and "2",
also we provided the default case in case the user misspells the the answer.

This solution is much more elegant,
and far easier to read.
Don't you think so too?

That concludes this section of refactoring.
There is a bit more I wish to talk about,
so let's move to the next part.

Next: [No Need for Atoms](7-no-need-for-atoms.md)
