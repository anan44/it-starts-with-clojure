# Hands on REPL

Now that we have successfully learned how to start REPL and evaluate code in it,
it is good to get familiar with some core functions from Clojure.

But before we get started,
let's get tiny bit of terminology sorted out.
In Lisps we evaluate _forms_.
Form is a data object to be evaluate as a program.
In most of the cases in Clojure this means something like this

```clojure
(function param1 param2)
```

Forms come in few shapes and colors,
but basically we have

- Self-Evaluating Forms
  - These evaluate to them selves.
    For example a vectors and lists are self-evaluating forms.
- Function Forms
  - These are forms that call a function.
    Example above would be one of these.
- Special forms
  - These are forms that differ from Clojure's standard evaluation rules,
    and are understood directly by the compiler.
    _def_, _if_, _new_, _and_ _do_ are examples special forms.
- Macros
  - These are functions that manipulate forms,
    thus they are a bit different from all the above.
    In this guide we wont be covering much about Macros.
    In case you are interested learning more about them,
    there are many sources to read about them.
    You will find some sources from the end of this file.
    That being said,
    I strongly recommend leaving Macros for later for now
    You should visit them when you have a  good understanding of the language.

## Some basic functions and data types

To get started with REPL let's get to know some basic functions in Clojure.
Try using your sandbox project's REPL through your IDE in this part.
This will help you to get comfortable with your programming environment.

### [str](https://clojuredocs.org/clojure.core/str)

[comment]: # (TODO: There is too much detail here. Needs to be leaned down)

I might have guessed it.
_str_ turns given arguments into string and concatenates them.
str takes n number of arguments and always returns a single string.
If no arguments are provided,
an empty string is returned.

Try str in your REPL.

```clojure
(str "REPL" "is" "cool")
;=> "REPLiscool"
```

As you might have noticed we use double quotes to express a string.
Unlike many languages Clojure does not allow expressing strings with single quotes.
In the case you need to use an actual double quote character in your string,
you have to escape it using an escape character \

Like this:

```clojure
(str "Tim \"tpope\" Pope")
;=> "Tim \"tpope\" Pope"
```

Notice how the escape characters are still there in the return value.
You will always see them in the string presentation of Clojure,
but they will be gone when we output the data to somewhere.
Let's try outputting the string to stdout with println function.

```clojure
(println "Tim \"tpope\" Pope")
Tim "tpope" Pope
;=> nil
```

Notice how clojure returns _nil_ after printing Tim "tpope" Pope?
This is because in clojure every form evaluates into something.
Functions evaluate into their return values.
println function (like print function) always return nil regardless of what is being printed.
Unlike in Java or many other languages,
Clojure does not support returning void.
Many places that would in other langauges return void return _nil_ instead.
It is good to keep in mind that nil is also a return value nevertheless.

Clojure's strings are actually Java strings.
Much of Clojure code is closely connected to Java.
This alone is a super interesting topic and many books have written whole chapters about it,
but we won't let it sidetrack us any more than this now.

If you are interested connection between Java and Clojure, read more from Clojure docs [Hosted on the JVM](https://clojure.org/about/jvm_hosted)
and [Java Interop](https://clojure.org/reference/java_interop).
As before, there are both things I suggest you leave for later.

In addition to println Clojure also provides handy function [prn](https://clojuredocs.org/clojure.core/prn).
prn is much like print, but it prints everything in machine readable format.

```clojure
(prn "Tim \"tpope\" Pope")
"Tim \"tpope\" Pope"
;=> nil

This can be especially handy while debugging.

## [+](http://clojuredocs.org/clojure.core/+)

Unlike in many other languages,
Clojure's arithmetic operators are functions just like any other.
They take arguments are return a value as result.

Symbol + is a name of a sum function just like any other name.
Function + takes n arguments and returns the sum of those arguments.

```clojure
(+ 10 10 5)
;=> 25

(+ 3)
;=> 3

(+)
;=> 0

(+ 10 5.5)
;=> 15.5
```

## [-](http://clojuredocs.org/clojure.core/-)

Subtraction function - takes n number of arguments.
It subtracts all the rest of arguments from the first argument.
If only one argument is provided,
the negation of that number is returned.
Unlike sum(+) subtraction(-) requires at least 1 argument,
or it throws an error.

```clojure
(- 10 3 2)
;=> 5

(- 3 10)
;=> -7

(- 1)
;=> -1
```

## [*](http://clojuredocs.org/clojure.core/*)

I hope at this point you are already starting to get a grasp on how arithmetic operators wok in clojure.
Product function * takes n number of arguments and returns the product of these arguments.
If called without arguments 1 is returned.

```clojure
(* 10 5)
;=> 50

(* 5 5 100)
;=> 2500

(* 0.02 300)
;=> 6.0

(*)
;=> 1
```

## [/](https://clojuredocs.org/clojure.core/_fs)

Division operator / is a bit more interesting than it peers,
since it nicely leeds us to diversity of Clojure's number formats.
This is a sidetrack that we are going to briefly visit now.

/ takes n number of arguments.
First of the arguments is the numerator and the rest are denominators.
The return value is the numerator divided by all the denominators.
What makes / interesting is the format of the return values.

```clojure
(/ 10 5)
;=> 2
```

In case the division is even a full number is returned.

```clojure
(/ 1 3)
;=> 1/3
```

But in the case where the division would lead to non-full number,
instead of a decimal a ratio is returned.
This is a huge boon in comparison to languages like Javascript,
which has numeric system that enables nonsense like this:

```javascript
# Javascript code

((1 / 3) + 1 - 1) * 3
;=> 0.9999999999999998
```

This will not happen in Clojure thanks to the ratio type.
This leeds us nicely to the topic of numbers in Clojure.
Don't worry we will be brief.

## [Numbers](https://clojure.org/reference/data_structures#Numbers)

As a [dynamic language](https://en.wikipedia.org/wiki/Dynamic_programming_language) Clojure does not require nor allow typing as does for example Java or Go.
By default numbers in Clojure are Java's long primitives.
This can be proven by evaluating following form in REPL:

```clojure
(type 3)
;=> java.lang.Long
```

([type](https://clojuredocs.org/clojure.core/type) is a helpful function that return a type of given argument.)

If the number is too large to fit to Long (>9223372036854775807),
then BigInteger data type is used.

```clojure
(type 9223372036854775808)
;=> clojure.lang.BigInt
```

Decimal numbers on the other hand are stored in Java doubles.

```clojure
(type 1.3)
;=> java.lang.Double
```

Convenient ratio type you already saw previously.

```clojure
(type 1/3)
;=> clojure.lang.Ratio
```

It is possible to force ratios into decimals to [floating points](https://en.wikipedia.org/wiki/Double-precision_floating-point_format) with the function double,
but is is recommended to operate with ratios if possible for enhanced precision.

```clojure
(double 1/4)
;=> 0.25
```

Additionally clojure also offers BigInt and BigDecimal numerics that can be specified with postfix N or M.

```clojure
(type 10M)
;=> java.math.BigDecimal

(type 10N)
;=> clojure.lang.BigInt
```

But you are unlikely to need these in near future,
nevertheless it is good to know that they are there.

## [Other notable data structures](https://clojure.org/reference/data_structures)

Clojure has few other key data types that you should be aware of.
The list does obviously not cover all the data types,
but these are few you should know that exists.
There is not need to have a proper understanding of all of them,
but it is good to know what their key features are and the they exist.
Do not stress if you have trouble wrapping your head around them,
we will visit all of them in greater detail in near future.

Incase you wish to learn more about given data structures before we cover them in this guide,
just follow the links to the official documentation.

### [nil](https://clojure.org/reference/data_structures#nil)

nil is Clojure's equivalent of None type in python or Null type in Java, C# or Javascript.
It is the representation of lack of data.

You can check if piece of data is nil with function _nil?_

```clojure
(type nil)
;=> nil

(nil? nil)
;=> true
```

### Booleans

Like almost all the other languages Clojure has two boolean values,
namely true and false.
If the value in question is true or false can be checked with functions true? and false?.

```clojure
(true? false)
;=> false

(true? true)
;=> true

(false? false)
;=> true

(true? 10)
;=> false
```

Additionally it might be interesting to know that evaluates all values except nil and false as true.
This is especially handy when playing with [boolean logics](https://en.wikipedia.org/wiki/Boolean_algebra).
We will of course cover this in greater detail later,
so there is again no need to stress over it.

### [Keywords](https://clojure.org/reference/data_structures#Keywords)

Clojure has an interesting data type known as Keywords,
which is missing from most of the other languages.
For now you can think of keywords mostly as weird cousin of string.
It is popular especially as key values in maps,
but it has also other usages.

```clojure
(type :keyword)
;=> clojure.lang.Keyword
```

Don't worry too much about keywords for now.
Their usefulness will become clear for you in time.

### [Lists](https://clojure.org/reference/data_structures#Lists)

Lists are as the name says lists of things.
The things in question can be rather much anything.
I am sure you have run to similar structure in the past with you other programming languages.

The [Syntax](https://en.wikipedia.org/wiki/Syntax) for lists in Clojure can be a bit confusing for new comers.
Even though list in Clojure does actually look like this (1 2 3),
when you refer to it in your code you need lead it with a single quote '(1 2 3).
This is due the fact that all the code in Clojure is in form of lists (remember it is a [Lisp](https://en.wikipedia.org/wiki/Lisp)).
So by default the language tries to evaluate the list as it would a function.
But since number 1 is not a function in fact, without the single quote you would get an error.

```clojure
'(1 2 3)
;=> (1 2 3)

(1 2 3)
;=> Execution error (ClassCastException) ...
java.lang.Long cannot be cast to clojure.lang.IFn

(type '(1 2 3))
;=> clojure.lang.PersistentList
```


You might also have noticed that we did not use commas to separate the values from each other.
Unlike other languages Clojure's compiler does not require such separators.
In fact Clojure's compiler will just ignore such commas even if you would provide them.
Even though using commas works,
it is generally considered idiomatic to leave them out.

```clojure
'(1, 2, 3)
;=> (1 2 3)
```

To recap:
When writing lists in to your code, use a leading single quote.

The error we had before:

```text
SOMETHING cannot be cast to clojure.lang.IFn
```

is rather common sight in Clojure.
It might seem cryptic at first,
but what it is trying to say is rather simple.

The thing SOMETHING could not be casted to clojure.lang.IFn,
which is short for **Interface Function**.
IFn is Clojure interface that all functions share and is required from the in order to be callable.

So in short: *SOMETHING was not a function*.

With this knowledge in your pocket you will avoid a lot of beginner's Clojure frustration.

### [Vectors](https://clojure.org/reference/data_structures#Vectors)

Vectors is a data type that is not a very common in most of other programming languages.
You may think of Vectors as sibling of lists.
Just like lists the vectors take multiple pieces of data to them and are extremely convenient ways to store and handle data.
Now you might think,
_Why does Clojure have two similar structures for list like structures?_
Well there is kinda significant difference in the performance of lists and vectors under different circumstances.
Then you might ask, _"Well which one should i use?"_
The rule of thump is:
When in doubt, use vectors.

We will later explain in better detail the performance differences between the two,
but until then (and probably afterwards too) you should prefer vectors over lists.
This should come naturally to you,
since the syntax for vectors is the same as for lists or arrays in most other languages.

```clojure
(type [1 2 3])
;=> clojure.lang.PersistentVector
```

Just like with lists it is idiomatic not to use separator commas with vectors.

### [Maps](https://clojure.org/reference/data_structures#Maps)

You have probably come across maps before in your previous programming adventures with your old favorite language.
In some languages like Python this marvelous data structure is known as Dict or Dictionary.
(In Javascript it is known as Object).
But do not let this fool you since essentially all of these are more or less the same thing.

Maps are collections of key-value-pairs.
In such pairs the first value is called a key and the second one a value.
There is no limitations regarding the types of values or keys in a single map.
Feel free experiment with any kind of combinations.
You may even try using different data types as all of keys,
Clojure won't care.

Like in lists and vectors it is idiomatic not to use commas as separators,
but it is acceptable if it makes code more readable.
It is also idiomatic to use keywords as keys when possible.

```clojure
(type {:name "Venkat" :age 51})

(type {:numbers [1 2 3] :chars [\a \b \c] :name "Rich Hickey" 3 "is a number"})
clojure.lang.PersistentArrayMap
```

If you have hard time wrapping your head around Clojure's map structure,
think of them as cooler cousin of JSON format.
They share a lot in common and are used for storing data in similar manner.

It is also good to note that Maps do not guarantee any specific order of the key-value-pairs in them.

### [Sets](https://clojure.org/reference/data_structures#Sets)

Sets are much like maps but without value part.
They are convenient for making sure you have only single entity of each piece of data in your collection.

```clojure
(type #{"Mars" "Jupiter" "Saturn"})
```

Like with lists and vectors it is idiomatic to leave separator commas out of sets.

Also like maps sets wont guarantee any specific order with them.

### [Collections](https://clojure.org/reference/data_structures#Collections)

For now think of collections as an umbrella data type for all sets, lists, maps and vectors.
If it fits many things,
it is a collection.
Simple as that.

You can check if something is in dead a collection with the function _coll?_

```clojure
(coll? [1 2 3])
;=> true

(coll? #{:a :b :c})
;=> true

(coll? 3)
;=> false

(coll? "Rich Hickey")
;=> false
```

This is I want you to be familiar with mostly because I will be using this term a lot,
and I don't what you to get confused.
You will master collections shortly,
so again: No need to lose sleep over them.

### [Characters](https://clojure.org/reference/data_structures#Characters)

A single character in Clojure is represented like this

```clojure
\j

(type \j)
;=> java.lang.Character
```

Notice lack of any kind of quotes. There is not much else to say about characters.

## Finishing with REPL

I hope you have been playing with REPL while we been going through these things.
It is crucial to get comfortable with your tooling,
so that when we dive deeper into the language you will won't have issues with using it.

There is few more things you should definitely learn about REPL,
but we will leave those for the later time when you more acute need for them.

In case you run into trouble or want to learn more about REPL immediately,
don't hesitate look into the official [REPL guide](https://clojure.org/guides/repl/introduction).
But as said this is probably not be necessary right now,
and you might want to press onwards towards the actual good stuff.

Use REPL as often as you can and you will quickly turn it into a formidable tool for your future adventures with Clojure.

## Sources for learning about macros

- [Clojure's documentation](https://clojure.org/reference/macros)
- [Clojure for Brave and True](https://www.braveclojure.com/writing-macros/)
- [Programming Clojure](https://pragprog.com/book/shcloj3/programming-clojure-third-edition)
- [Living Clojure](https://www.oreilly.com/library/view/living-clojure/9781491909270/)

Next: [Chapter 3 - Your First Project](../3-first-project)
