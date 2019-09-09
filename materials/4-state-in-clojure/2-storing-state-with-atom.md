# 2. Storing state with atom

Now that we are familiar with data structures and adding data to them,
we still need few more things to store the state of the application.

## [def](https://clojuredocs.org/clojure.core/def) - saving constants

defs let us define what most languages call variables.
With def we can do the same as with let,
but instead of function the values will be usable from the whole namespace.

defs are really simple actually:

```clojure
(def x 10)
(def y 20)

(+ y x)
=> 30
```

There is not much more to say about defs for now.
Almost anything can be stored to them.
Not only values and objects but functions too.
This is because Clojure's functions are [first class citizens](https://en.wikipedia.org/wiki/First-class_function),
which means that they can be treated as any other values.
They can be passed as arguments or used as return values.
You have already done this with map function,
where we gave a function as parameter,
so this should not be completely new to you.

All the values defined with def are immutables,
like constants and finals are in other languages.
Due to this we will need something extra to get around with the mutable state.
That something is atom.

## [atom](https://clojuredocs.org/clojure.core/atom)

Atom is a special object that we can define and later modify with functions.
With atoms Clojure applications can take care of state in concurrent applications with out much hustle.
If you have tried doing this in other languages like Java or Python,
might know that it is not completely trivial task usually.
[Race condition](https://en.wikipedia.org/wiki/Race_condition) is a good example of an issue caused by poorly managed state.
We won't be doing concurrency on this guide,
since it is way out of our scope.
But I wanted you to know why such an unusual tool as atom is the goto tool for saving mutable variables in Clojure.

Let's do a quick number incrementation exercise to get familiar with the functionalities of atom.

We will define an atom called _number_ and give it a value of 1

```clojure
(def number (atom 1))
```

To see the value in an atom we need to use a special operator @.

```clojure
@number
=> 1
```

@ is actually a short hand for the function [deref](https://clojuredocs.org/clojure.core/deref),
but you will almost never use deref since @ is so clear.
Nevertheless it is something you should know of.

Altering a value in an atom might feel a bit tricky,
in the beginning but it is quite simple actually.
To alter a value we need to provide the atom a function that will take the current value as a parameter,
and then the return value will be stored as the new value for the atom.
This whole thing will be done using [swap!](https://clojuredocs.org/clojure.core/swap!) function.
Notice that exclamation mark is part of the function name.
Exclamation mark is often used to notify that the function has side effects.

### [swap!](https://clojuredocs.org/clojure.core/swap!)

swap! takes atom and a function as parameters.
It can also take additional parameters which will also be passed to the function.

To increment a number the idiomatic way in Clojure is to use [inc](https://clojuredocs.org/clojure.core/inc).
It is a function that takes a number and returns that number +1.

```clojure
(swap! number inc)
=> 2
(swap! number inc)
=> 3
@number
=> 3
```

The number can also be decreased by using inc's counterpart [dec](https://clojuredocs.org/clojure.core/dec)

```clojure
(swap! number dec)
=> 2
```

If we want to add number to our atom that is easy too.
As mentioned before,
swap! can also take additional arguments that will be passed to the function

```clojure
(swap! number + 5)
=> 7
```

### [reset!](https://clojuredocs.org/clojure.core/reset!)

The atom's value can also be completely reset to new value with reset! function.
This function should be used mostly when the atom needs to be reset regardless of its current value.
If current value plays any role in resetting of the value,
is usually better to use swap!

```clojure
(reset! number 1)
=> 1
@number
=> 1
```

### few words about atom

Atom is definitely a handy tool,
but it is good to acknowledge that is not needed that often.
Clojure's functional approach to programming is able to solve issues without using any mutable state.
I simply wanted to introduce this tool to you,
so you won't waste your time wondering how certain things can be accomplished with Clojure (as I did when I was learning the basics).
Try not to over use atom.
It is there for you if you need it,
but it is a power that you should not overuse.

Another thing that you should know about atom,
is that it might perform the same function more than once to your value.
Thus you should never use functions with any side effects with atoms,
since those side effects might take place multiple times.
This is because of the internals of the atom that make sure that no race conditions occur.

When we call swap! atom stores the current value it holds and puts it aside,
then it performs the given functions with its current value.
before setting the new value to atom it checks if the current value is still the same as it put aside in the beginning.
If something else has managed to change the current value of the atom in the meanwhile,
it will start from the beginning using the new value as parameter.
This is how atom guarantees that no race conditions occur,
but it also may cause your function to run multiple times.

Atom also has cousins [ref](https://clojuredocs.org/clojure.core/ref) and [agent](https://clojuredocs.org/clojure.core/agent),
which are both somewhat similar but a bit different.
They are solutions to state problems that atom cannot solve.
They are needed very rarely and you should stick to atom when ever possible.
Due to rarity of their use cases I won't include them into this guide.
You know they exist and you know where to look for them if you happen to need them.

## Updating shopping list

Now we should know enough to be able to create a shopping list that we can add new items to.
Define an atom called _shoppings_ to our shopping-list.core and initialize it as an empty vector

```clojure
(def shoppings (atom []))
```

With that being done,
we can create a function that we can add elements to shoppings.
You should already posses all the necessary skills to do so,
but will give you hand just in case.

```clojure
(defn add-shopping
  [shopping]
  (swap! shoppings conj shopping))
```

This function can take any sort of object as a parameter and it will add it to the shoppings.
Remember to try it from REPL.

```clojure
(add-shopping {:product "Milk" :amount "1 bottle"})
=> [{:product "Milk" :amount "1 bottle"}]

(add-shopping {:product "Candies" :amount "1 bag"})
=> [{:product "Milk", :amount "1 bottle"} {:product "Candies", :amount "1 bag"}]
```

Great! It works.
Now with this being done we will have few more steps to complete the program.

We still need to create some kind of terminal interface that will prompts for values from user.
Also we need to somehow save our list to a file (and hopefully in human readable format).
These are the tasks that we will struggle with in next parts.
