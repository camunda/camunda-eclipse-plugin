Setup a RCP Application
=======================
----------------------

###Requirements
The RCP application in this tutorial is build with Eclipse Juno.

* Setup your Eclipse for the camunda Modeler.
* Make sure that you can build an Eclipse plugin of the modeler.


###Build a Product
* Add a Product configuration file to the modeler project.

![New Product](https://raw.github.com/camunda/camunda-eclipse-plugin/develop/org.camunda.bpm.modeler.tests.functional/documentation/images/Jubula_RCP_NewProduct.png)

----------------
* Select `org.camunda.bpmn.modeler` as parent folder.
* Enter _modeler.product_ as file name.
* Select `Use a launch` configuration.

![Product Configuration](https://raw.github.com/camunda/camunda-eclipse-plugin/develop/org.camunda.bpm.modeler.tests.functional/documentation/images/Jubula_RCP_NewProductConfiguration.png)

----------------
* Open the Product Configuration Editor.
* Enter a product ID.

![Product Overview](https://raw.github.com/camunda/camunda-eclipse-plugin/develop/org.camunda.bpm.modeler.tests.functional/documentation/images/Jubula_RCP_Product_Overview.png)

----------------
* Open the Dependencies tab.
* Select `Remove All` to clear the Plug-ins list.
* Select `Add...` and enter _org.camunda.bpm.modeler_.
* Select `Add Required Plug-ins`.
* Add the following plug-ins manually:
	* _org.eclipse.core.net_
	* _org.eclipse.ui.ide.application_
	* _org.eclipse.rcp_

![Product Dependencies](https://raw.github.com/camunda/camunda-eclipse-plugin/develop/org.camunda.bpm.modeler.tests.functional/documentation/images/Jubula_RCP_Product_Dependencies.png)

----------------
* Go back to the Overview tab and use the Eclipse Product export wizard to build your RCP application.
* Configure the wizard as shown in the following picture.

![Product Export](https://raw.github.com/camunda/camunda-eclipse-plugin/develop/org.camunda.bpm.modeler.tests.functional/documentation/images/Jubula_RCP_Product_Export.png)

----------------
* In the product Overview you have the possibility to test the application. Please note that your RCP can be used although the test failed, so try to run the exported RCP product.

###Configure your AUT
After you have build the RCP application you have to provide access for Jubula to control the AUT.

* In the Jubula program folder you will find a rcp-support.zip package. Extract it and copy the _org.eclipse.jubula.rc.rcp_ plugin into the plugin folder of the RCP application.
* To activate the plugin open the 'configuration\configuration.ini' file and add _org.eclipse.jubula.rc.rcp_ to the list of osgi.bundles.

![AUT configuration](https://raw.github.com/camunda/camunda-eclipse-plugin/develop/org.camunda.bpm.modeler.tests.functional/documentation/images/Jubula_RCP_configINI.png)


for Eclipse Kepler add `reference\:file\:org.eclipse.jubula.rc.rcp@start` to the list of osgi.bundles.