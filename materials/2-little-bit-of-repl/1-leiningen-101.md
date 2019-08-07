# Leiningen: The basics

Leiningen is a great and powerful tool used in Clojure.
It is automated build tool and dependency manager.
Along your journey to Clojure you will get more and more familiar with it,
but for now we are going to cover only the very basics.

For more details visit [Leiningen tutorial](https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md).

## Your very own project

Lets create you a very real Clojure project,
where you can mess around as much as you want.
We call this project **sandbox**.
For the rest of the guide feel free to use this project for anything you wish to try out.
We will not build anything to this project after this chapter (2-little-bit-of-repl),
so there is no reason to worry if you will mess up something that is going to be needed later.

### Creating project

When we call Leiningen from terminal we use short version **lein**.
To create a new project we use following pattern:

```bash
lein new [template-name] [project-name]
```
**Template-name** is non-obligatory.
If left empty a default template is used.
**Project-name** will be the name of the project that is being created.

Now navigate to your desired location in your terminal and there write following command:

```bash
lein new sandbox
```

Assuming you have everything installed correctly as instructed in previously,
you should end up with a project looking something like this:

```bash
.
└─ sandbox
  ├── CHANGELOG.md
  ├── LICENSE
  ├── README.md
  ├── doc
  │   └── intro.md
  ├── project.clj
  ├── resources
  ├── sandbox.iml
  ├── src
  │   └── sandbox
  │       └── core.clj
  ├── target
  │   ├── classes
  │   │   └── META-INF
  │   │       └── maven
  │   │           └── sandbox
  │   │               └── sandbox
  │   │                   └── pom.properties
  │   └── stale
  │       └── leiningen.core.classpath.extract-native-dependencies
  └── test
      └── sandbox
          └── core_test.clj
```

This is all that we are going to cover here at this point,
but we will be returning to leiningen later when we will be building something.
