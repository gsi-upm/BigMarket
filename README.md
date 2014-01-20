BigMarket
=========

Developer's Guide
=================

Necessary tools:

Eclipse IDE for Java EE Developers
http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr1

Apache Tomcat (BigMarket run properly with Apache Tomcat 6):
http://tomcat.apache.org/download-60.cgi

Gephi:
https://gephi.org/

The .jars that you need are included in the project.

Installation:

First of all you need to install some plugins in Gephi. To do this open Gephi and in Tools>Plugins install:
  -JSONExporter
  -Graph Streaming
  -Complex Generators

Once you have this you can follow this tutorial to install and configure properly Apache Tomcat 6:
http://tomcat.apache.org/tomcat-6.0-doc/setup.html

Create a new Runtime in eclipse in order to run the project:
http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.jst.server.ui.doc.user%2Ftopics%2Ftwtomprf.html

Now you can import the project and run it:

-Import the project from github (we recommend use Egit, a Eclipse plugin to import Git projects, install it 
from Eclipse in Help>Install new Software)
-Add Apache Tomcat to the libraries of the project (Right click on the project BuildPath > Configure Build path)
-Right click on index.html (WebContent>index.html) Run as > Run on server
-Select Choose an existing server
-Finish

Now eclipse will open a HTML page in which you can set the simulation parameters (actually to run the simulation properly you have to select this options):
  -No Twitter Dataset
  -Simulation with GUI
  -In number of nodes you can put any number (take care because a high number can slow the simulation)
  
Click in start simulation and the program open a MASON simulation. Now the simulation is ready to run it and you can take two ways: 
  -Connect it with Gephi: you have to open Gephi and open a new project, now in Streaming tab, right click on Master server and select Start and in the MASON main window go to Model tab and mark GephiFlag box. Now run the simulation
  -Simply run the simulation: run the simulation. 



