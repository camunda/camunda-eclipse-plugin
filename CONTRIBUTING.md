# How to contribute

* Fork the project
* [Setup Eclipse IDE](#setup-eclipse-ide)
 * [Install system plugins](#install-system-plugins)
 * [Install project plugins](#install-project-plugins)
 * [Setup modeler projects](#setup-modeler-projects)
* [Inspect the project structure](#project-structure)
* Write some code
* Submit a pull request. [Contribution guidelines](https://github.com/camunda/camunda.org/blob/master/COMMIT_MESSAGES.md)

## Setup Eclipse IDE

This was tested with Eclipse for RCP and RAP Developers (Indigo SR2 and Juno SR2).

### Install system plugins

In this section we install general plugins.

* Install `EGit` using the Update Site `http://download.eclipse.org/egit/updates-2.3` or Eclipse Marketplace

### Install project plugins

In this section we install specific plugins to build the camunda modeler without errors and get the projects ready for development. 

The following shows a screenshot of an Eclipse Indigo installation with the required plugins installed.
Highlighted are plugins which need to be installed in order to resolve add dependencies. 

![Development Requirements](https://raw.github.com/camunda/camunda-modeler/develop/documentation/images/development-requirements.png)

To avoid any problems install the plugins in the following order:

1. EMF plugins
2. Graphiti plugin
3. BPMN2 plugin
4. Eclipse plugins
5. Maven plugin

#### Installation "Walk through" 

##### Indigo

* Open `Help > Install new software ...`
* Choose `Indigo - http://download.eclipse.org/releases/indigo`
* Open category `Modeling` and install:
 * `EMF - Eclipse Modeling Framework SDK` - 2.7.2.v20120130-0943
 * `EMF Validation Framework SDK` - 1.5.0.v20110502-1600-67O-96SGR55YJeZPedcQnghI6uFA
 * `Graphiti SDK (Incubation)` - 0.8.2.v20120215-0845
* Add the Graphiti Update Site: `http://download.eclipse.org/graphiti/updates/0.9.2/`
 * Search for updates to bring Graphiti to the required version
* Add the BPMN 2.0 Update Site: `http://download.eclipse.org/modeling/mdt/bpmn2/updates/milestones/S20130423/` 
 * install `BPMN2 Project Feature` - 0.7.0.201304230617
* Choose `Indigo - http://download.eclipse.org/releases/indigo`
* Open category `Web, XML, Java EE and OSGi Enterprise Development` and install
 * `Eclipse Web Developer Tools` - 3.3.2.v201111030500-7O7IFj6EMjB7yO1Xs_G1kMtQeOye6HTXFWve95_R
 * `Eclipse Java EE Developer Tools` - 3.3.2.v201111030500-7b7II1YFSK2WIuPRDEnExPV-RvTn
* Open the eclipse marketplace
 * search for `maven`
 * install `Maven integration for Eclipse`
* Open `Window -> Preferences -> Maven -> Discovery -> Open Catalog`
 * search for `tycho`
 * install `Tycho Configurator`
* Optional: Add `http://winterwell.com/software/updatesite/` and 
 * install `MarkDown Editor`
 
##### Juno

* Open `Help > Install new software ...`
* Choose `Juno - http://download.eclipse.org/releases/juno`
* Open category `Modeling` and install:
 * `EMF - Eclipse Modeling Framework SDK` - 2.8.3.v20130125-0826
 * `EMF Validation Framework SDK` - 1.6.0.v20120328-0001-67T-96SGR75UNqZNTdeQnghI6uFA
 * `Graphiti SDK (Incubation)` - 0.9.2.v20130211-0913
* Add the BPMN 2.0 Update Site: `http://download.eclipse.org/modeling/mdt/bpmn2/updates/milestones/S20130423/` 
 * install `BPMN2 Project Feature` - 0.7.0.201304230617
* Choose `Juno - http://download.eclipse.org/releases/juno`
* Open category `Web, XML, Java EE and OSGi Enterprise Development` and install
 * `Eclipse Web Developer Tools` - 3.4.2.v201301240600-7b7JINbFSK2WM3QNAEr799V-TyUp
 * `Eclipse Java EE Developer Tools` - 3.4.2.v201211061806-7O7MFu3EMkBK01NbrfU9ATE5cdZFz-OoeYjaI4d2
* Open the eclipse marketplace
 * search for `maven`
 * install `Maven integration for Eclipse`
* Open `Window -> Preferences -> Maven -> Discovery -> Open Catalog`
 * search for `tycho`
 * install `Tycho Configurator`
* Optional: Add `http://winterwell.com/software/updatesite/` and 
 * install `MarkDown Editor`

### Setup modeler projects

* Checkout the `develop` branch
* Make sure you have the `m2eclipse` plugin installed in your Eclipse
* Import sources into Eclipse Indigo or Juno via `Import -> Existing maven projects`
* Apply the camunda bpm [formatter templates](https://github.com/camunda/camunda-bpm-platform/tree/master/settings/eclipse)
* If you have already installed the [required project plugins](#install-project-plugins) no error should occur. 
* [Examine the sources](#project-structure)
* Run / Test
    * Start the Modeler from `org.camunda.bpm.modeler` via `Run As -> Eclipse Application`
    * Run the test suite in `org.camunda.bpm.modeler.tests` via `Run As -> JUnit Plugin-Tests`

### Project Structure

* org.camunda.bpm.modeler -> modeler runtime
* org.camunda.bpm.modeler.tests -> modeler runtime tests
* org.camunda.bpm.modeler.tests.functional -> functional GUI tests (jubula)

[1]: https://github.com/camunda/camunda-modeler
