Tomcat required
https://tomcat.apache.org/tomcat-7.0-doc/appdev/installation.html
And it has to be added to the IDE used, e.g. for NetBeans
http://www.compcoding.com/docs/spring/netbeansAddTomcat/netbeans-add-tomcat.jsp
Refernce to servlet-api.jar (in tomcat) can be needed to compile

Previous steps

First of all you must download Eclipse:

http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr1

This versión must have Egit plugin install (that’s not necessary but it helps you to import Git projects). Instead this version doesn’t include Egit you can download it from this URL:

http://www.eclipse.org/egit/download/

Install it from Help>Install new Software

Now you need to install Apache Tomcat (in this software we use Apache Tomcat v6.0). You can download it from this URL:

http://tomcat.apache.org/download-60.cgi

Follow this tutorial to install and configure it properly:

http://tomcat.apache.org/tomcat-6.0-doc/setup.html


Now you have all the necessary tools to run and develop BigMarket.

Import the git project in Eclipse

If you want to run the project like a Web project you must create a new server runtime:

Windows>Preferences>Server>Add

Select Apache tomcat v6.0 click next, and select the Apache Tomcat v6.0 parent directory.

Now you can run the project:

-Right click on index.html (WebContent>index.html)>Run as> Run on server

-Select Choose an existing server (created in the previous step)

-Finish