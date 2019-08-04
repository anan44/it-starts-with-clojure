# Introduction: Rebel with a REPL

In 1-first-things-first/introduction I said that we will focus on real world applications,
but it would be incredibly irresponsible of me not to start with REPL.
Many programming languages start with classic hello world printing,
but in Clojure we like to start from [REPL (Read-Eval-Print-Loop)](https://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop).
Even though REPL does exist in many other languages,
as a dialect of Lisp (stands for List Processor) Clojure's REPL is quite powerful tool,
and thus it is used constantly in Clojure development.

There are many ways to use REPL in Clojure.
Here we are going through two of them.

## REPL in command line

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

When opening a file from Clojure project with VSC,
the IDE automatically opens a REPL connection for you.
This is quite convenient and not super well documented in the clojure plugin, 
so lets test it out together in our sandbox project.

Open the sandbox project in your VSC.
Not just the file, but the whole project.

In the VSC navigate to the file sandbox/src/sandbox/core.clj.

Here write following code at the bottom of the file:

```clojure
(str "hello" "-" "parens")
```

Now move your cursor to this line and select
**"Clojure: Eval and show the result"**
from the the command pallet.
Command pallet can be accessed with **cmd+shift+p** (or **ctrl+shift+p** depending on your computer).

Now the results should be visible in the output tab at the bottom of the editor.

```bash
=> "hello-parens!"
```

If you are going to be using VSC as recommended,
it is higly recommended to setup a convenient keyboard shortcut for this operation.
Keyboard shortcuts can easily be accessed via command pallet.
Just open the pallet and search for the work shortcut.

## REPL in Cursive

TODO - Write instructions for using REPL in Cursive
