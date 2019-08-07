# [Minimum viable product (MVP)](https://en.wikipedia.org/wiki/Minimum_viable_product)

Lets get started with your first Clojure project.
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

## Lets start coding

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
You might have noticed some kind of __defn__ form in sandbox project previously.
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

>Same as (def name (fn [params* ] exprs*)) or (def
>name (fn ([params* ] exprs*)+)) with any doc-string or attrs added
>to the var metadata. prepost-map defines a map with optional keys
>:pre and :post that contain collections of pre or post conditions.

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
Instead of such statement,
Clojure functions return the output of the last thing evaluated.

Ok thats again enough babbling and academics.
Lets take the practical approach and write some functions.