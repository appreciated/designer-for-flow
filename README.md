<p align="center"><img src="https://github.com/appreciated/designer-for-flow/blob/master/src/main/webapp/frontend/styles/images/logo-floating-low.png">
<br>
  <h1>Designer for Flow</h1>
</p>    

This is a Application which allows to create and edit Views dependening on the Vaadin Platform.

Currently the following output and input formats being supported are:
- Java Classes    

# Executing
Before starting the application via IDE execute `mvn clean install`.

For developement purposes the Application can be run using `mvn spring-boot:run` or directly by running the `com.github.appreciated.designer.Application` class from your IDE. 

# Building
To build the project run the following maven command:  
`mvn clean install -Pproduction` 

After executing the ready to use electron application can be found under `target/electron/designer-for-flow-<archtype>`.

# Branching

* `master` the latest version of the starter, using the latest platform snapshot
* `v15` the version for Vaadin 15

# License

This Project is licensed under the GNU Lesser General Public License v3.0
