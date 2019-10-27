# [2. Running and building with Leiningen](https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md)

So far we have used Leiningen for creating projects from templates,
and running REPL for our repl code.
But Leiningen is actually very powerful and diverse tool capable of many more things.

## [Running with Leiningen](https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md#running-code)

Next natural step will be running code with Leiningen.
This is the defacto basic way of running Clojure code from command line.

Navigate to the root of our sum-em-up project and run the bellow command from the command line.

```clojure
lein run
;=> 325
```

If this won't work for some reason,
make sure that you completed all the steps from the previous part.
Also remember to create the numbers.txt file to the root of your project.

But all in all running clojure projects with Leiningen is rather straight forward.

## [Building with Leiningen](https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md#uberjar)

Running your project with Leiningen is often not enough.
It has fundamentally two issues.

1. It requires you to deploy the whole project source code

2. It requires the deployment environment to have Leiningen, Clojure and all the dependencies installed

Both of these issues kinda suck.
Luckily Leiningen has more functionalities available for us.

Solution for both of these issues is the Uberjar.
Uberjars are [JAR](https://en.wikipedia.org/wiki/JAR_(file_format)) files with all the necessary dependencies embedded.

Uberjar still requires that the deployment environment has Java (JRE) installed,
but this is rather easy requirement to fill.
Since so many things use Java,
JRE is often already available.
If it is not then it is not hard to install.

So how do we create an uberjar?
Navigate again to the root of our project and execute:

`lein uberjar`

After this we can execute this with following command:

```clojure
java -jar target/uberjar/sum-em-up2-0.1.0-SNAPSHOT-standalone.jar
;=> 325
```

Remember that there has to be the numbers.txt file in the project root folder when you execute this.
(assuming that you execute it from project root folder)

Since at the very core Clojure is just another Library,
it will be able to build normal jar files.
At their core these files are just like any other Java program you might come across.

With this we have covered running and building projects with Leiningen.

Next: [Refactoring The Project](3-refactoring-project.md)
