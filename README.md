# Overview
An app to manage configuration and visualisation of connected Git repositories

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

2. Run Maven install from the Maven panel *(right side of the screen) in IntelliJ 

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
