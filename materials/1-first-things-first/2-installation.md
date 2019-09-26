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

Installation of Java on Linux varies by distribution.
On Fedora, the package names and even the package manager has changed over time.
On Ubuntu there are similar complications.
However, if you're smart enough to work out Linux,
you can work out how to install Java.

On Ubuntu 18.04:

```sh
sudo apt-get update
sudo apt-get install openjdk-11-jdk
```

Getting Java 12 on Ubuntu 18.04 requires a PPA and is probably more trouble than an intro guide needs to go into.
Maybe Ubuntu 19.04 repositories have a later Java version. IDK.

On Fedora 30 (and later):

```sh
sudo dnf install java-1.8.0-openjdk-devel
# OR:
sudo dnf install java-11-openjdk-devel
# OR:
sudo dnf install java-latest-openjdk-devel # Currently, Java 12
```

Clojure installation:

On any Linux distribution, 
you can get the latest Clojure by following the [instructions](https://clojure.org/guides/getting_started) on clojure.org.

Ubuntu 18.04: you can get Clojure 1.9 with sudo apt-get install clojure

Fedora 30: sudo dnf install clojure gets you Clojure 1.7.0. 

## Mac

My rule of thumb for Mac is to install everything with brew if possible with ease.
Some might disagree and they are free to do so.
Feel welcome to write non-brew part of Mac installations here.

### Install homebrew

As mentioned above this installation guide leans heavily on homebrew (a.k.a. brew).
Thus we will start by installing the said brew.

The official instructions are available [here](https://brew.sh).

But really the only thing oyu need to do is to execute this:

```sh
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

That being done you have brew installed ony your Mac.

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

Next: [Clojure and IDEs](3-clojure-and-IDE.md)
