BPMN 2.0 Modeler for Eclipse
============================

&copy; 2011, 2012 camunda services GmbH


Licensing
---------

Licensed under the Eclipse public license <http://www.eclipse.org/legal/epl-v10.html>. 

How to contribute
-----------------

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

![Development Requirements](https://raw.github.com/camunda/bpmn2-modeler/develop/documentation/images/development-requirements.png)

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

[1]: https://github.com/camunda/bpmn2-modeler
