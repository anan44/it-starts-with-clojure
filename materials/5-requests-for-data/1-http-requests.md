# 5.1 HTTP Request

As usual start by initializing a new project with Leiningen:

```sh
lein new app reddit-analyser
```

Nothing new here.
Let's move forward.

As usual we are going to have a ton of new cool stuff coming up in this chapter.
In fact we are having that many new cool things that I had a slight problem deciding what to start with.
After some pondering I thought it would be cool to start with [HTTP requests](https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol).

For performing performing http requests with Clojure we are going to be using an external module.
This module is [dakrone/clj-http](https://github.com/dakrone/clj-http).

Before we can actually get started with the clj-http itself,
we have to briefly cover another topic.

## Clojars

So far we have only used dependencies from within Clojure's source code,
but this time we are using an external dependence.
For this we have to learn how to deal with external dependencies in our Leiningen project.

We are also going learn about [Clojars](https://clojars.org/),
Clojure's repository for open source libraries.
In other words Clojars is the place that you want to be looking for dependencies for your project.
It is a great place to start,
when you suspect there might exist a library for the code problem you are having.

Using Clojars is dead easy.
Just write a search word to the search field and see what the repository has to offer.
You can browser through options from the easy UI.
Many projects have links to their git repository where you can see their source code.
Additionally you have the information regarding how many times the given dependency has been downloaded.

Another very basic information is the dependency code that you can directly copy to your project,
so you can effortlessly use a given dependency.

Since we are using Leiningen we will be using the snippet from labeled as **Leiningen/Boot**.
As you can see there are also other options for dependency handling, namely Gradle and Maven.
These are great tools for Java or Kotlin,
but with Clojure I would advice you to stay away from them.

## 3rd Party Dependencies

Now that we know enough about Clojars we can move forward.
Next we will search for clj-http from Clojars.
This should show us [following page](https://clojars.org/http-kit).

Depending when you are doing this you should see something like this as Leiningen/Boot snippet:

```clojure
[clj-http "3.10.0"]
```

Clojure is rather heavily leaning to backwards compatibility,
so there is a very good chance that our codes are going to work with newer versions as well.
But for reason you will run into weird bugs that you cannot explain,
try using the exact same versions as I am using here.

In order to use this library,
we'll have to add this snippet into our project's dependencies in project.clj file.
