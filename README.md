# cycle-manager-client <img src="/src/main/resources/io/github/danielzyla/pdcaclient/img/logo.png" width="100">

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Usage](#usage)

## General info
This application is designed to support user in practice management of projects by usage of commonly known PDCA approach.

PDCA (plan–do–check–act) is an iterative design and management method used in business for the control and continuous improvement of processes and products. It is also known as the Deming cycle or Shewhart cycle.

> **Cycle-manager-client** is a desktop GUI application that allows the user to interact from the local computer with external REST application endpoints.

The application provides:
- chronological record of activities
- control of the sequence of steps in the cycle
- cycles sequence control
- ease access  to the content of completed cycles
- the ability to run multiple projects at the same time
- separate modules to define basic resources (employees, departments, products)
- the ability to use the same resources in multiple projects
	
## Technologies
Project is created with:
* Java 11
* Maven
* JavaFX
* Scene Builder
* Spring Boot (starter-web)
* TestFX
	
## Usage
To run this project dedicated API is required. Currently, the compatible interface can be found here:
```
https://pdca-cycle-manager.herokuapp.com
```
The URL must be included in the properties of the application.
