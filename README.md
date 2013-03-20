Camunda Modeler
==============

The camunda modeler is a BPMN 2.0 modeling plugin for Eclipse which focuses on seamless modeling of Process and Collaboration diagrams.
It is part of the [camunda BPM](http://camunda.org) software stack.

![Screenshot](https://raw.github.com/camunda/camunda-modeler/develop/documentation/images/shot.png)

Features
------------

* Native BPMN 2.0 support
  * Reads and writes BPMN 2.0 diagram files
  * Shows import warnings and errors
  * Stores diagram information in DI
* Layouting
  * Advanced connection layouting (manhattan connection)
* Property Tabs
  * For BPMN 2.0 properties
  * For [camunda engine](http://camunda.org/implement.html) and [Activiti](http://activiti.org) extensions

Resources
---------------

* [Download](http://camunda.org/download.html) / [Eclipse Plugin Update Site](http://camunda.org/release/camunda-modeler/update-sites/latest/site/)
* [User Forum](https://groups.google.com/forum/#!forum/camunda-bpm-users)
* [Development Forum](https://groups.google.com/forum/#!forum/camunda-bpm-dev)

How to contribute
------------------------

* Fork the modeler sources
* Checkout the `develop` branch
* Make sure you have the `m2eclipse` plugin installed in your Eclipse
* Import sources into Eclipse Indigo or Juno via `Import -> Existing maven projects`
* Resolve project dependencies (see below)
* Examine the sources
    * `org.camunda.bpm.modeler` contains the modeler
    * `org.camunda.bpm.modeler.tests` contains the modeler test suite
* Run / Test
    * Start the Modeler from `org.camunda.bpm.modeler` via `Run As -> Eclipse Application`
    * Run the test suite in `org.camunda.bpm.modeler.tests` via `Run As -> JUnit Plugin-Tests`
* Extend the editor **!**
* Submit a pull request

Modeler Development Dependencies
--------------------------------

The following shows a screenshot of an Eclipse Indigo installation with the required plugins installed.
Highlighted are plugins which need to be installed in order to resolve add dependencies. 

![Development Requirements](https://raw.github.com/camunda/camunda-modeler/develop/documentation/images/development-requirements.png)

Click image to enlarge.

### Versions and Update Sites ###

* __Graphiti__ 0.9.1 http://download.eclipse.org/graphiti/updates/0.9.1/
* __BPMN 2.0 Meta-Model__ 0.7.x http://download.eclipse.org/bpmn2-modeler/bpmn2/site/
    * Sources [via git](git://git.eclipse.org/gitroot/bpmn2) + workspace import

Project Structure
-----------------

    + org.camunda.bpm.modeler -> modeler runtime
    + org.camunda.bpm.modeler.tests -> modeler runtime tests
    + org.camunda.bpm.modeler.tests.functional -> functional GUI tests (jubula)


Licensing
-------------

Licensed under the Eclipse public license <http://www.eclipse.org/legal/epl-v10.html>. 

[1]: https://github.com/camunda/camunda-modeler
