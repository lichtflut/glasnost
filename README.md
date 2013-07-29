#Glasnost#

Resource Based Hub for Enterprise Information

Copyright 2011-2013 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH

Licensed under Apache License version 2.0

see http://www.apache.org/licenses/LICENSE-2.0.txt

## Short "How To" build and run Glasnost ##

Preparations:

* Java 7 installed
* Maven 3 installed
* Ruby >= 1.8 installed
* Tomcat 7 installed

To build the Glasnost web application, check out this project, change to root directory and type

`mvn package`  

Afterwards you will find a `glasnost.war` inside the `war/target` directory. You can deploy this application by dropping it
in the Tomcat's `webapps` directory.
