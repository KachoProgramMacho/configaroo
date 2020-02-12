# Overview

Configaroo is a tool developed in partnership with IAV. It allows effortless management of large distributed code bases. The prototype version of the tool enables the easy and straightforward organization of Github repositories and submodules by utilizing graph visualization functionality. In particular, Configaroo depicts the relationships between repositories and submodules as edges in a dependency graph. As visualization is one of the primary features of the tool, Configaroo supports a large variety of different graph layouts and allows dependency rendering ahead of time, so that developers can pick and choose the right set of modules for their final project configuration.

## Prerequisite Tools:
*In brackets you can find the versions used during development.*

* npm (6.13.6)

* Node (10.16.2)

* Angular CLI (8.3.15)

* Angular (8.2.12)

* Maven (3.3.9)

* Java 8

* Git

# Getting Started 

## Installation:
The first step is cloning the project:

```shell
git clone https://github.com/KachoProgramMacho/configaroo.git
cd configaroo 
```

After cloning the project, to run Configaroo locally follow these steps:

#### Starting the Backend:
```shell
cd backend
mvn package
java -jar target/gitmodconfig_backend-0.0.1-SNAPSHOT.jar
```

#### Start the Frontend:
```shell
cd frontend
npm install
ng build
npm run dev
```

## Development Environment Setup / Contributing:

If you are interested in setting up your local development enviroment to either play around or extend the functionality of the project follow the steps below:

#### Setup the Backend Dev Environment:

1. In IntelliJ click on Import project and select the project "pom.xml" file:

![alt text](https://github.com/KachoProgramMacho/configaroo/blob/master/documentation/images/configarooIntellijSetup.png)

2. Run "install" from the Maven panel *(right side of the screen)* in IntelliJ 

3. Right click on the "GitmodconfigBackendApplication" class and press "Run GitmodconfigBackendApplication":

![alt text](https://github.com/KachoProgramMacho/configaroo/blob/master/documentation/images/ConfigarooBackendSetup.png)


#### Setup the Frontend Dev Environment:

*To edit the project one can use any editor or IDE. Some examples include Atom, Visual Studio Code, Sublime Text, etc.*

```shell
cd frontend
npm install
code . # Opening the code with Visual Studio Code
ng serve
```






## Authors
Denis Rangelov [Github Account 1](https://github.com/LukchoZloto), 
               [Github Account 2](https://github.com/d-rangelov)
               
Martin Kachev [Github Account](https://github.com/KachoProgramMacho)
