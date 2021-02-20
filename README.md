<p align="center"><img src="https://raw.githubusercontent.com/appreciated/designer-for-flow/master/src/main/resources/META-INF/resources/img/logo-floating-low.png">
<br>
<h1>Designer for Flow</h1>
</p> 

This is a WYSIWYG-Editor that allows creating components for Vaadin flow.

Currently the following input and output formats are supported:
- Java Classes    

# Maturity & Current status
The Editor is currently in alpha (or pre-alpha), it still has quite a fex bugs. Since the development speed decreased over time, I decided to release it before its completion. I hope that around this editor will develop a community willing to improve and test it. I no longer have the time to develop this product with the wanted speed, so no promises here. 

# Concept
The packed Editor will run a Vaadin instance inside electron, using a shipped JDK.

# Distribution
The Editor is not meant to be run manually. [Instead use the prepackaged Releases](https://github.com/appreciated/designer-for-flow/releases).

# Versioning
The Version of the Editor is required to be coupled with the respective Vaadin version being used in a project. To make it easy for the user to identify which vaadin Version the designer is required a scheme as followed will be used. 

designer-for-flow v14.0.* -> Vaadin 14.0  
designer-for-flow v14.1.* -> Vaadin 14.1  

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

This Project is licensed under the GNU General Public License v3.0
