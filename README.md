# smeup-rpg

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/smeup/smeup-rpg.svg?branch=master)](https://travis-ci.org/smeup/smeup-rpg)
[![CircleCI](https://circleci.com/gh/smeup/smeup-rpg.svg?style=svg)](https://circleci.com/gh/smeup/smeup-rpg)
[![codebeat badge](https://codebeat.co/badges/92633ae2-5640-47b3-a0e7-b324f68288ac)](https://codebeat.co/projects/github-com-smeup-smeup-rpg-master)
[![](https://jitpack.io/v/smeup/smeup-rpg.svg)](https://jitpack.io/#smeup/smeup-rpg)

This project contains an interpreter for RPG, which runs on the JVM. The interpreter is written in Kotlin.

## How to compile the code

The project uses gradle and a gradle wrapper is included. That means that you can simply execute `./gradlew` (on Mac/Linux) or `gradlew.bat` (on Windows) to run it.

The project contains an ANTLR grammar from which a parser in Java is generated. To generate it run:

```
./gradlew generateGrammarSource
```

The code can then be compiled with:

```
./gradlew build
```

IDEA project files can be generated by running:

```
./gradlew idea
```
Then import in IDEA using these options:

![Alt text](docs/setup/idea.png?raw=true "Idea import project options")

**It's very important not to check "Create separate module per source set"!!!**

[Here is a short video on how to setup a Linux workstation to develop this project](https://youtu.be/4Kd1b-VPTEs)

## Running tests

All tests (except for performance tests) can be executed by running:

```
./gradlew check
```

To run performance tests (i.e. tests tagged with the annotation `@Category(PerformanceTest::class)`) run:
```
./gradlew testPerformance
```

To run all tests:
```
./gradlew testAll
```


If you want to force the execution of all checks:

```
./gradlew check -rerun-tasks
```

(_Side note: if you get this error running tests_
 ```
 com.esotericsoftware.kryo.KryoException: Buffer underflow
 ```
 _try to clean the .gradle directory_)

## Creating a jar with all dependencies to run some examples
You can create a jar that includes all the dependencies:

```
./gradlew fatJar
```

This will produce the file

``` 
rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar
```

So you can run an RPGLE file from the command line this way (after moving to the directory that contains this jar):

``` 
java -jar rpgJavaInterpreter-core-all.jar path/to/my/RPGLE [parameters]
```

[In this short video you can see how to run the examples](https://youtu.be/llw2vNeupA4)

If you omit the program name, you will be able to call programs in a [simple shell](https://youtu.be/uNd6h5H2wTM).

## How the repository is organized

* _generated-src_ contains the code generated from the grammar
* _gradle_, _gradlew_, and _gradlew.bat_ contain the gradle wrapper
* _src_ contains the source code for the project and the tests
* _out_, and _build_ contain temporary files
* _misc_ contains utilities for downloading sources from AS400 (for example [this ruby script](misc/ftpas.rb))
* _misc/docker_: support for [Docker, see instructions](misc/docker/docker.md)
* _docs_ contains documentation


## How to use this code in your project

At the moment, we use [Jitpack](https://jitpack.io/) to publish the [project](https://jitpack.io/#smeup/smeup-rpg).

If you use Maven, add these lines to your pom.xml in order to add the repository

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
	
Then add the following dependencies for the core library:
	
    <dependency>
        <groupId>com.github.smeup.smeup-rpg</groupId>
        <artifactId>rpgJavaInterpreter-core</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>

And this if you want to include the examples too:
		
    <dependency>
        <groupId>com.github.smeup.smeup-rpg</groupId>
        <artifactId>examples</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>

Side note for maven users who use mirrors: remember to change your .m2/settings.xml with settings like this:

    <mirrors>
        <mirror>
            <id>myNexus</id>
            <mirrorOf>!jitpack.io,*</mirrorOf>

## Credits

The grammar used in this project is based on the work from [Ryan Eberly](https://www.linkedin.com/in/ryan-eberly-428b438/). It is derived from his project [rpgleparser](https://github.com/rpgleparser/rpgleparser).

Some RPG Examples are from [Claudio Neroni](https://www.neroni.it) 

Another source for good examples is [go4as400](http://www.go4as400.com)

