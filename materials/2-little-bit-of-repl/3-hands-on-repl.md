# Hands on REPL

Now that we have successfully learned how to start REPL and evaluate code in it,
it is good to get familiar with some core functions from Clojure.

But before we get started,
lets get tiny bit of terminology sorted out.
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

I might have guessed it.
_str_ turns given arguments into string and concatenates them.
str takes n number of arguments and always returns a single string.
If no arguments are provided,
an empty string is returned.

Try str in your REPL.

```clojure
(str "REPL" "is" "cool")
=> "REPLiscool"
```

As you might have noticed we use double quotes to express a string.
Unlike many languages Clojure does not allow expressing strings with single quotes.
In the case you need to use an actual double quote character in your string,
you have to escape it using an escape character \

Like this:

```clojure
(str "Tim \"tpope\" Pope")
=> "Tim \"tpope\" Pope"
```

Notice how the escape characters are still there in the return value.
You will always see them in the string presentation of Clojure,
but they will be gone when we output the data to somewhere.
Lets try outputting the string to stdout with println function.

```clojure
(println "Tim \"tpope\" Pope")
Tim "tpope" Pope
nil
```

Notice how clojure returns _nil_ after printing Tim "tpope" Pope?
This is because in clojure every form evaluates into something.
Functions evaluate into their return values.
println function (like print function) has no return value,
thus _nil_ is returned.

Clojure's strings are actually Java strings.
Much of Clojure code is closely connected to Java.
This alone is a super interesting topic and many books have written whole chapters about it,
but we won't let it sidetrack us any more than this now.

If you are interested connection between Java and Clojure, read more from Clojure docs [Hosted on the JVM](https://clojure.org/about/jvm_hosted)
and  [Java Interop](https://clojure.org/reference/java_interop).
As before, there are both things I suggest you leave for later.

## [+](http://clojuredocs.org/clojure.core/+)

Unlike in many other languages,
Clojure's arithmetic operators are functions just like any other.
They take arguments are return a value as result.

Symbol + is a name of a sum function just like any other name.
Function + takes n arguments and returns the sum of those arguments.

```clojure
(+ 10 10 5)
=> 25

(+ 3)
=> 3

(+)
=> 0

(+ 10 5.5)
=> 15.5
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
=> 5

(- 3 10)
=> -7

(- 1)
=> -1
```

## [*](http://clojuredocs.org/clojure.core/*)

I hope at this point you are already starting to get a grasp on how arithmetic operators wok in clojure.
Product function * takes n number of arguments and returns the product of these arguments.
If called without arguments 1 is returned.

```clojure
(* 10 5)
=> 50

(* 5 5 100)
=> 2500

(* 0.02 300)
=> 6.0

(*)
=> 1
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
=> 2
```

In case the division is even a full number is returned.

```clojure
(/ 1 3)
=> 1/3
```

But in the case where the division would lead to non-full number,
instead of a decimal a ratio is returned.
This is a huge boon in comparison to languages like Javascript,
which has numeric system that enables nonsense like this:

```javascript
# Javascript code

((1 / 3) + 1 - 1) * 3
=> 0.9999999999999998
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
=> java.lang.Long
```

([type](https://clojuredocs.org/clojure.core/type) is a helpful function that return a type of given argument)

If the number is too large to fit to Long (>9223372036854775807),
then BigInteger data type is used.

```clojure
(type 9223372036854775808)
=> clojure.lang.BigInt
```

Decimal numbers on the other hand are stored in Java doubles.

```clojure
(type 1.3)
=> java.lang.Double
```

Convenient ratio type you already saw previously

```clojure
(type 1/3)
=> clojure.lang.Ratio
```

It is possible to force ratios into decimals to [floating points](https://en.wikipedia.org/wiki/Double-precision_floating-point_format) with function double,
but is is recommended to operate with ratios if possible for enhanced precision.

```clojure
(double 1/4)
=> 0.25
```

Additionally clojure also offers BigInt and BigDecimal numerics that can be specified with postfix N or M.

```clojure
(type 10M)
=> java.math.BigDecimal

(type 10N)
=> clojure.lang.BigInt
```

But you are unlikely to need these in near future,
nevertheless it is good to know that they are there.

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
