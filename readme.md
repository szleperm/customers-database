#customers-database

Example CRUD RESTful web application with AngularJS front-end

Used technologies:

Server Side:
* Spring MVC
* Spring Data
* Hibernate
* HSQLDB
* JUnit
* Mockito
* Maven

Client side:
* AngularJS
* Bower
* Grunt

To get the code:
-------------------
Clone the repository:

    $ git clone git://github.com/szleperm/customers-database.git

To run the application:
-------------------	
Front-end in this application is based on Node.js, Bower and Grunt, so the first requirement is to have npm (Node Packaged Module) installed on your machine. If you don't have it yet, install Node.js from the [Node.js website](https://nodejs.org/) (prefer an LTS version). This will also install npm.

From the command line:

    $ cd customers-database/front
    $ npm install && bower install && grunt && cd ..
    
Now you can run application from the command line with maven:

    $ mvn jetty:run 

or

In your preferred IDE such as SpringSource Tool Suite (STS) or IDEA:

* Import customers-database as a Maven Project
* Run on server in your IDE

Access to the application
-------------------------

Default access the web application at: http://localhost:8080/customers-database/

Default access the REST API at: http://localhost:8080/customers-database/api/customers