# Introduction: Rebel with a REPL

In 1-first-things-first/introduction I said that we will focus on real world applications,
but it would be incredibly irresponsible of me not to start with REPL.
Many programming languages start with classic hello world printing,
but in Clojure we like to start from [REPL (Read-Eval-Print-Loop)](https://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop).
Even though REPL does exist in many other languages,
as a dialect of Lisp (stands for List Processor) Clojure's REPL is quite powerful tool,
and thus it is used constantly in Clojure development.

There are many ways to use REPL in Clojure.
In a moment we are going through two of them.

## [What is REPL actually](https://clojure.org/guides/repl/introduction)

Before we spoke about REPL but we did not really cover what it is or why it used.
So typically in Clojure the development cycle slightly differs from many other languages,
especially Object Oriented Programming (OOP) languages.
I won't go to the details of the difference here,
but one of the main reasons why Clojure's REPL is so powerful is because FP applications have very little [state](https://en.wikipedia.org/wiki/State_(computer_science)).

In typical a OOP application the development cycle is often Test Driven Development (TDD).
In TDD we often write a test call for the functionality we aim to create,
and then create the functionality so that the test passes.
Or it is sometimes possible write the functionality first and see if it works by manual testing.
This often involves running the whole application,
which may be quite time consuming.

REPL allows the us to evaluate individual functions without running the whole application.
When the application does not really have state,
or most of the code base does not heavily rely on the said state,
it is possible to develop functions *in a void*.
FP programming relies on so called [Pure Functions](https://en.wikipedia.org/wiki/Pure_function),
which always return the same output with same inputs.
I am not going to go into the details of Functional Programming or Pure Functions here.
In case you are interested to learn more,
read the article above.

## [REPL in command line](https://clojure.org/guides/repl/launching_a_basic_repl#_leiningen)

Using REPL from command line is perhaps the easiest way to use REPL,
but it is not very convenient.
Nevertheless, we are going to quickly go through it,
since it is definitely useful skill sometimes.

Lets navigate to the sandbox project we created previously.

```bash
cd sandbox
```

When you are inside the project, simply request lein to open you a REPL.

```bash
lein repl
```

After this something like this should appear to your terminal:

```bash
nREPL server started on port 63715 on host 127.0.0.1 - nrepl://127.0.0.1:63715
REPL-y 0.4.3, nREPL 0.6.0
Clojure 1.10.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_202-b08
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e

sandbox.core=>
```

Now that we have our REPL open,
lets try if it works with a simple form:

```clojure
(+ 1 1)
```

After pressing enter the result (2) should be shown in terminal.

Using REPL like this is easy,
but you don't necessarily be typing your whole function in constantly.
Because of this I would recommend using REPL from your IDE.

## REPL in VSC

First launch REPL in terminal with Leiningen as we did previously.

Open the sandbox project in your VSC.
Not just the file, but the whole project.

Open VSC Command Pallet.
Command pallet can be accessed with **cmd+shift+p** (or **ctrl+shift+p** depending on your computer).
From Command Pallet select **Calva: Connect to a running REPL**.
The editor will now ask you few questions, which should be easy to answer (just press enter).

After the connection is made navigate to the file sandbox/src/sandbox/core.clj,
and write following code at the bottom of the file:

```clojure
(str "hello" "-" "parens")
```

Now move your cursor to end of this line and select
**": Evaluate current top level form, and print to output"**
from the the command pallet.

Now the results should be visible in the output tab at the bottom of the editor.

```bash
=> "hello-parens!"
```

Calva provides plenty of REPL options and this is only one of them.
Feel free to toy around with different options if you feel like trying them out.

Calva provides plenty of keyboard shortcuts, explore is they are suitable for you,
if not VSC enables easy modification of keyboard shortcuts.
Keyboard shortcuts can easily be accessed via command pallet.
Just open the pallet and search for the work shortcut.

### WARNING for Vim mode users

In case you are using Vim mode in VSC,
you need to remap Calva's "Clear all inline display evaluations" shortcut,
which is set to ESC by default.

If you are not using Vim mode in VSC, you don't have to worry about this.

## REPL in Cursive

TODO - Write instructions for using REPL in Cursive