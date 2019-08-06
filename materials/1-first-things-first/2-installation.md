# Installing Clojure

Installing Clojure can be sometimes be more difficult than it should be.
In general you always need three basic parts.

1. Java
2. Clojure
3. Leiningen (build automation and dependency management tool)

Even though Clojure itself supports latest version of Java,
many libraries still do not.
To avoid some pain with this we will be using Java 1.8.
Feel free to use newer versions.
If things stop working try changing your Java version.

## Linux

TODO

## Mac

My rule of thumb for Mac is to install everything with brew if possible with ease.
Some might disagree and they are free to do so.
Feel welcome to write non-brew part of Mac installations here.

### Install Java 1.8

```bash
brew update
brew tap adoptopenjdk/openjdk
brew cask install adoptopenjdk8
```

### Install Clojure

```bash
brew update
brew install clojure
```

### Install Leiningen

```bash
brew update
brew install leiningen
```

### Install jEnv (non-obligatory)

Many of us need newer Java versions for our other projects.
To handle multiple Java versions I recommend using jEnv.
jEnv is wonderful little tool for deciding,
which Java version you wish to use in each project.

```bash
brew update
brew install jenv
```

jEnv requires some configuring.
Please see instructions from their [website](https://www.jenv.be/).
It is really easy, so there is no reason to worry.

## Windows

TODO
