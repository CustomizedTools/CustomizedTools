


CST Version 1.0 Release Notes:
==============================

What's the CustomizedTools
--------------------------
* A troubleshooting intelligent tool, integrate with GCViewer, samurai. CustomizedTools can analyse java thread dump, JVM GC log, JBoss log and configuring file, CustomizedTools also can test Database connection, JMS server connection, find class from jars, monitor a folder, etc

* A easy to customize your tool, *HelloWorldTool* is a demo to illustrate how to define you own tool, more detail please refer below 'HelloWorldTool example for how to customize your own tools' section

* More about CustomizedTools: https://github.com/kylinsoong/CustomizedTools/blob/master/docs/index.asciidoc


How to Build
------------

* Maven 3.0 or later & Ant 1.7 or later intalled and configured

* build depdency project, clone GCViewer from https://github.com/chewiebug/GCViewer, build, install archive to local repository:

mvn clean install

* mvn build source code:

mvn clean dependency:copy-dependencies install

* ant build distribution

ant

`dist` foulder will be generated, this is the final distribution. You can either run CustomizedTools through `cts.sh` or `cts.bat`, or add CustomizedTools as servive, start as `service cts start`


*HelloWorldTool* example for how to customize your own tools
-----------------------------------------------------------

* Your own code show extends `com.customized.tools.AbstractTools`, implement your tool logic in execute() method, `Tools.xml` can help you pass parameters which in implement your tool logic,  take `HelloWorldTool.java` as example:

* Add your tools name add reference code name to `tools.properties`, for helloworl tool add bellow 2 line:

HelloWorldTool=com.customized.tools.HelloWorldTool

HelloWorldTool.prompt=Hello World Tool

NOTE: each tool name property has a prompt property, *prompt property* = *name* + ".prompt"

* Build CustomizedTools, run HelloWorldTool will be in all tools list, select HelloWorldTool, the following will be prompt:

Hello Wrold Tool Test

