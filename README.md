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
https://pdca-cycle-manager.herokuapp.com/api
```
The URL must be included in the properties of the application.

Prior registration in the application is required. Click [here](https://pdca-cycle-manager.herokuapp.com/signUp) to sign up.
Each request to the REST application is made using a token obtained as a result of user authentication.
After logging in, you can use the application until the token expires (up to 15 minutes).

Four modules are available to user: Project, Product, Department, Employee. For each of them new entities can be created and saved in external database.
<img src="/src/main/resources/io/github/danielzyla/pdcaclient/img/project-list.png">\
In each module, the selected item can also be edited or deleted.
<img src="/src/main/resources/io/github/danielzyla/pdcaclient/img/edit-project.png">\
All the entities introduced within the modules can be further used and shared within Cycle which is sub-module of Project.
All cycles created within a given project are available in the Cycle. They can be selected, viewed and edited. 
<img src="/src/main/resources/io/github/danielzyla/pdcaclient/img/cycles-view.png">
<img src="/src/main/resources/io/github/danielzyla/pdcaclient/img/cycle-edit.png">
The cycles are created automatically and the cycle cannot be deleted. However, you can delete their contents as long as they are in "pending" status.
