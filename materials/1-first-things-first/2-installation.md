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

### Installing Java 1.8 on Linux

Installation of Java on Linux varies by distribution.
On Fedora, the package names and even the package manager has changed over time.
On Ubuntu there are similar complications.
However, if you're smart enough to work out Linux,
you can work out how to install Java.

On Ubuntu 18.04:

```sh
sudo apt-get update
```

On Fedora 30 (and later):

```sh
sudo dnf install java-1.8.0-openjdk-devel
```

### Installing Clojure on Linux

On any Linux distribution you can get the latest Clojure by following
the [instructions](https://clojure.org/guides/getting_started#_installation_on_linux) on clojure.org.

### Installing Leiningen on Linux

On any Linux distributionyou can get the latest Leiningen by following
the [instructions](https://leiningen.org) on leiningen.org

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

### Install Java 1.8 on Mac

```bash
brew update
brew tap adoptopenjdk/openjdk
brew cask install adoptopenjdk8
```

### Install Clojure on Mac

```bash
brew update
brew install clojure
```

### Install Leiningen on Mac

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

If regardless of my advice or for absolute necessity you decide to pursue the Windows installation,
follow the instructions below.
I will not try to write exact instructions here,
but instead I'll provide sources you should use.
This is due to the fact that these things might change,
and I don't use Windows,
so I cannot guarantee that the instructions stay up-to-date.

## Windows

Clojure on Windows is not on very mature state,
so if you have an option to use Linux or Mac I would advise you to do so.

### Installing Java 1.8 on Windows

Follow the instructions from [adoptopenjdk.net](https://adoptopenjdk.net/).

### Installing Clojure on Windows

Follow the instructions from official Clojure [documentation](https://clojure.org/guides/getting_started#_installation_on_windows).

### Installing Leiningen on Windows

Follow the instructions from official Leiningen [documentation](https://leiningen.org/)

Next: [Clojure and IDEs](3-clojure-and-IDE.md)
