# [Minimum viable product (MVP)](https://en.wikipedia.org/wiki/Minimum_viable_product)

Let's get started with your first Clojure project.
When building a software it is a good idea to get started with small,
in many cases the "small" refers to smallest thing that we can consider a working solution.
If the code is well written,
it is easy to expand its functionality.
This is exactly what we are going to do here.

## Creating the project with Leiningen

You should remember Leiningen from the time when we created your sandbox project.
What we will be doing now is quite similar, with a small adjustment.

Using your terminal navigate to a folder where you want to save your Clojure projects.

Create the project with following command:

```clojure
lein new app sum-em-up
```

Here we are telling Leiningen the would like to

- create a new project

- use template called "app"

- the project should be named "sum-em-up"

You might notice that only thing really different from our sandbox is the _app_ part.
We are asking Leiningen to create the project suitable for application.
This means that there will be few things that the _default_ project does not have.
Most importantly main function and some project settings in project.clj.
We could easily use the default template and make the necessary changes by ourselves,
but why would we not use the nice functionalities that have been provided for us?

The project you created should look something like this:

```plain-text
.
├── CHANGELOG.md
├── LICENSE
├── README.md
├── doc
│   └── intro.md
├── project.clj
├── resources
├── src
│   └── sum_em_up2
│       └── core.clj
├── sum-em-up2.iml
├── target
│   └── default
│       └── classes
└── test
    └── sum_em_up2
        └── core_test.clj
```

## Let's start coding

Now that we have successfully created our project,
we may actually get our hands dirty with some code.
For starters you should launch your REPL and open file src/sum_em_up/core.clj.
For this kind of simple project we can easily write all of our code to single file.

The file should look like this at the moment:

```clojure
(ns sum-em-up.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
```

For now we are going to ignore the part with ns and :gen-class.
Don't worry, you we will get comfortable with them too in some while.
That being said,
I believe that it is time to address the elephant in the room.
You might have noticed some kind of **defn** form in sandbox project previously.
Then we ignored it because we had more pressing matters,
and it is not possible to cover everything at once.

### [defn](https://clojuredocs.org/clojure.core/defn)

defn is perhaps the single most important form in the whole language.
As you might have heard,
Clojure is a Functional Programming (FP) language.
That means obviously means that functions are at the center of everything.
Reason why defn is so important,
is that is the tool we use to create new functions.

In the official documentation defn might look a bit intimidating:

```clojure
(defn name doc-string? attr-map? [params*] prepost-map? body)
```

And the explanation does not make it much easier:

> Same as (def name (fn [params* ] exprs*)) or (def
> name (fn ([params* ] exprs\*)+)) with any doc-string or attrs added
> to the var metadata. prepost-map defines a map with optional keys
> :pre and :post that contain collections of pre or post conditions.

So instead of trying to make sens of this explanation,
we are going to simple it down quite a bit.

We will completely ignore _attr-map?_ and _prepost-map?_.
You won't be needing them anytime soon.

That leaves us with a bit less to explain.
I also took the liberty to re-organize this form for the sake of clarity.

```clojure
(defn name
  doc-string?
  [params*]
  body)
```

So now that is done, we can walk through all the parts of the form.

- name

  - Name of the function you are about to create.

- doc-string?

  - This part is non-obligatory, but it is recommended to use it for clarity.
    It is string where you explain what this function actually does.
    The compiler will ignore this string,
    so it is written for humans yourself and other programmers.

- [params*]

  - This is a vector of parameters that our function expects.
    Even if function will no expect parameters,
    an empty vector must be provided in definition.

- body
  - This is the part where the logic of the function lives.
    Every function has this part.

You might wonder where the return part is?
Most other programming languages have explicit return statement.
Instead of such a statement,
Clojure functions return the output of the last thing evaluated.

Ok thats again enough babbling and academics.
Let's take the practical approach and write some functions.

### Defining sum function

So if we would have a vector of numbers,
how would we sum them up?
In many other languages the obvious solution would be a for-loop.
In Clojure we do also have similar structures,
but we will not use them now.
Instead we are going to be using [apply](https://clojuredocs.org/clojure.core/apply) function.

Let's define the following function:

```clojure
(defn sum
  "Sums a vector of numbers"
  [a-vec]
  (apply + a-vec)`
```

Apply takes a function and a [sequence](https://clojure.org/reference/sequences) as parameters,
and it calls the function using contents of that sequence as parameters.

This is easier to understand when you see it like this:

```clojure
;; these are practically the same
(apply + [1 2 3])
=> 6
(+ 1 2 3)
=> 6
```

Sequence is an interface for many kind of collections in clojure.
Vectors, Lists and many others are sequenceable.
This means that they implement sequence interface,
which is something majority of Clojure's functions rely on.

We can check if something sequenceable with function _sequential?_

```clojure
(sequential? [1 2 3])
=> true
```

Now that we got that out of the way we can try if our function actually works.
It is a common development pattern in Clojure to define a function,
and then try it out in REPL.
If it works,
we can move forward.
If it wont,
we will redefine and try again.

Le's call our function to try it out.

```clojure
(sum [1 2 3])
=> Syntax error compiling at (core.clj:9:1).
Unable to resolve symbol: sum in this context
```

WHAT!! Didn't we just define sum?
Why is REPL unable resolve the [symbol](https://stackoverflow.com/questions/2320348/symbols-in-clojure)?
(symbol refers to an identifier).
Well the answer is simple,
we need to evaluate the defn form before REPL knows about it.
You might have written it to your file,
but it won't be loaded to your REPL session before you explicitly evaluate it.

Now proceed to evaluate the defn sum form,
just like you did with all the other forms in
[2.2. Using REPL](../2-little-bit-of-repl/2-using-repl.md)
and [2.3. Hands on REPL](../2-little-bit-of-repl/3-hands-on-repl.md).
After this you can try calling sum again.

```clojure
(sum [1 2 3])
=> 6
```

Great!
Our function works exactly as intended.
And congratulations for writing your first function.
It is definitely the most important step towards becoming an actual Clojurist.

**This pattern of writing and testing our functions is something you should always do when writing Clojure.**
Always keep your REPL open and ready.
Later on in this book we wont explicitly tell you to try out the functions we do,
but you should do so anyway.
Build up the habit of doing so.
Not only will it help you to understand what we are doing,
but having the habit will effectively make you a better Clojurista.
It is also a good idea to try different kinds of inputs for your functions,
to make sure they actually work as you intended.

With that being said,
let's get back to our project.

As you recently learned that apply works actually on all sequences (seq for short).
This means that our defn sum code is a bit misleading.
We state that it takes in a vector,
but it clearly works with many kinds of seqs.

```clojure
(sum '(1 2 3)) ;; This is a List if you have forgotten
=> 6
(sum #{1 2 3}) ;; and this is a Set
=> 6
```

Let's change our code a bit to make it more clear what our function is able to do.

```clojure
(defn sum
  "Sums a vector of numbers"
  [a-seq]
  (apply + a-seq)`
```

This wont actually change the functionality of our function at all,
but it makes it more clear that it can take other things than Vectors as well.
Also notice that we are calling the parameter _a-seq_ instead of _seq_.
This is because we don't want to [shadow](https://en.wikipedia.org/wiki/Variable_shadowing)
 an existing function [seq](https://clojuredocs.org/clojure.core/seq) and cause unnecessary confusion.
For the very same reason called the previous parameter a-vec (there is also a function called [vec](https://clojuredocs.org/clojure.core/vec)).
Alternatively,
we could call the parameter _coll_,
that would be suitable too and it is often seen in Clojure code.

Now that we have written a perfectly amazing sum function,
it is time to move onwards.

### Reading from a file: meeting with slurp

Reading from a file in Clojure is much easier than for example in Java or Python.
It is actually hilarious how easy it is.

Clojure provides us with a magnificent function known as a [slurp](https://clojuredocs.org/clojure.core/slurp).
The power of slurp is rivaled only buy the playfulness of its name.
So shall we give it a try?

Slurp file path as its sole parameter.
We will try it on the README file of your sum-em-up project.

```clojure
(slurp "README.md")
=> "# sum-em-up2

FIXME: description

## Installation..." ;; this actually goes on way more
```

As you can see from your own REPL,
slurp returns the whole file as a string.
Depending on the language you come from,
this might or might not be weird for you.
Returning the whole thing as a single string leaves a lot of responsibility for the programmer.
It is completely up to you to parse and process this file now as you please.

Before we move on to with our project,
I really have to demonstrate another cool feature of slurp to you.
slurp is not limited to reading files.
Among many other things it can be used for reading websites.

```clojure
(slurp "https://clojuredocs.org/")
=> "<!DOCTYPE html>
 <html><head><meta ..." ;; again I shortened this
```

If you need to read something from somewhere,
slurp is usually your best pal.

Ok. Now we know slurp and know how to use it too.
Since our plan is to read bunch of numbers from file,
it is fair enough to think that we need the said file.

Let's create a text file with some numbers to the root of our project.
If you are in the root of your project, you can just run this command on your terminal:

```bash
echo 1 2 3 4 5 10 300 > numbers.txt
```

(This won't work on windows,
so if you are a windows user you will have to create the file by hand.
Just create a file to root of the project with numbers 1 2 3 4 5 10 300 in it.
Don't separate numbers with any thing else than spaces.)

Now that we have our number file we can start move on.
Try reading the numbers.txt file with slurp from your REPL.

```clojure
(slurp "numbers.txt")
=> "1 2 3 4 5 10 300\n" ;; your file might or might not have \n in the end.
```

### Parsing string

So since you probably done some programming your life before this,
I believe you see an issue in summing single string of numbers.
To solve this we need to somehow turn this string of numbers into vector of numbers.

So we need a function that is able to perform this:

```clojure
(parse-numbers "1 2 3 4 5 10 300\n")
=> [1 2 3 4 5 10 300]
```

So let's try defining such a function.

First thing we want to do is to be able to somehow split this single string into many tiny strings.
Luckily Clojure's [string namespace](https://clojure.github.io/clojure/clojure.string-api.html)
has just the tool for us.
We won't dive deeper with [namespaces](https://clojure.org/reference/namespaces) just yet,
but if you are eager you can read about them from the link.

```clojure
(clojure.string/split "1 2 3 4 5 10 300\n" #"\s+")
=> ["1" "2" "3" "4" "5" "10" "300"]
```

So what does actually happen here?
Since split is not from clojure.core or from our namespace sum-em-up.core,
we have to specify to clojure where exactly is it located.
That is done with **clojure.string/** .
After that we just write the name of the function we are calling.
Next weird thing is **#"\s+"**.
This something known as [Regex](https://en.wikipedia.org/wiki/Regular_expression) pattern.
In clojure we can specify regex patterns by adding # in front of a string.
As you might know,
Regex is not Clojure specific thing,
but a handy tool found in one form or another from almost all modern programming languages.
If you are not familiar with Regex yet,
I strongly suggest you complete a short course on the topic at [RegexOne](https://regexone.com/) some time soon.
It should not take you more than an hour.

For those who are not familiar with Regex i will quickly explain what the Regex pattern \s+ means.
**\s** stands for any white space character,
those being space, tab (\t), carriage return (\r) and new lines (\n).
\+ on the other hand means that here may be 1 or more of characters/elements described before it.

In other words **#"\s+"** is a Clojure's way of describing a Regex pattern that says:

"Match as many whitespace characters as you please,
as long as there is at least one of them"

So our function call above is asking split the string "1 2 3 4 5 10 300\n"
from where ever there is any sorts of whitespaces, no matter how many there is.
This will nicely leave us with a vector of strings all representing numbers.
Nice right.

So let's define this into a function:

We are not done yet though.
If we would try to sum the numbers in this vector,
we would get an error.

```clojure
(sum ["1" "2" "3" "4" "5" "10" "300"])
=> Syntax error (ClassCastException) compiling at (core.clj:13:1).
class java.lang.
String cannot be cast to class java.lang.Number (java.lang.String and java.lang.Number are in module java.base of loader 'bootstrap')
```

This is because for obvious reasons strings cannot be summed up.

This takes us to the next two problems.

1. How do we parse a single string into a number?

2. How do we parse vector of strings to vector of numbers?

Let's get started with the first problem.

### String to Long

In Clojure we often utilize the power of underlying JVM.
This is known as [Java interop](https://www.braveclojure.com/java/).
Thanks to the fact that Clojure is built on top of JVM,
we can often utilize the [Java classes, objects and methods directly from Clojure code](https://clojure.org/reference/java_interop#_the_dot_special_form).
This also gives Clojure to harvest the power of Java ecosystem and its many packages.
This itself is a complex topic,
So I recommend that you only dive deeper into it when you need it.

For now we are just going through the bare minimum to get the job done.

Clojure's whole numbers are most commonly Java's Long type,
so to parse those it is natural to dip our toes into the world of Java interop to parse such a number from string.

In case you are not familiar with how this is done in java,
would look something like this.

```java
string numberInText = "100";
Long ourLong = Long.valueOf(numberInText);
```

To achieve this in Clojure we will use special syntax for Java interop.

```clojure
(Long/valueOf "303")
=> 303
```

Great! It worked!
So here we call a static method valueOf from class Long using a parameter "303".

That solves our first problem.
Let's take a shot at the second problem.
How do we apply this to a whole list of strings?

For this the tool would be our good friend [map](https://clojuredocs.org/clojure.core/map) function.
Map is a very flexible function that can be used in many ways,
but here we are going to use the simplest and most common way.

We will give map 2 parameters:
A function and collection.
What map will do is that it will apply the given function to each of the members of the collections,
and then returns us the collection of those results.
Let's give it a shot.

```clojure
(map Long/valueOf ["1" "2" "3" "4" "5" "10" "300"])
=> Syntax error compiling at (core.clj:17:1).
Unable to find static field: valueOf in class java.lang.Long
```

WHAT!?!?
This was not supposed to happen!
Well actually it kinda was.
map can only take Clojure functions as as parameters,
thus Java static methods are not accepted.
Well how will we solve this?

Luckily there is an easy way out of this ditch.
We can just simply wrap the static method call into a Clojure function and map will have no complaints.

```clojure
(defn str->long
  [x]
  (Long/valueOf x))

(str->long "1337")
=> 1337
```

So lets try our newly defined function with map.

```clojure
(map str->long ["1" "2" "3" "4" "5" "10" "300"])
=> (1 2 3 4 5 10 300)
```

YES!
Our solution worked.
Now we have all the necessary parts to complete our program.

In order to make the program complete,
as the next step we will build all the logic into main function,
so Leiningen will know in which order it should run our code.

## Building the main logic

When we created the project with Leiningen using the app template,
Leiningen created a function -main to our project.
This is the starting point of our Clojure application.
When we start running the project,
it starts from the beginning of the main function.
When the end of -main function is reached the program completes.
You might be familiar with this concept from other programming languages,
since it is rather common pattern.

Before we will now gather all the parts we have prepared under the main function:

```clojure
(defn -main
  [& args]
  (println (sum (map str->long (clojure.string/split (slurp "numbers.txt") #"\s+")))))
```

This is what we came up with.
It is not pretty but it works.
You can try calling this the main function from REPL

```clojure
(-main)
=> 325
```

This covers our minimum viable product.
Don't worry,
we won't leave our code in such an ugly state for very long.

The whole code should look like this at the end of this part:

```clojure
(ns sum-em-up.core
  (:gen-class)

(defn sum
  "Sums a vector of numbers"
  [a-seq]
  (apply + a-seq))

(defn str->long
  [x]
  (Long/valueOf x))

(defn -main
  [& args]
  (println (sum (map str->long (clojure.string/split (slurp "numbers.txt") #"\s+")))))
```
