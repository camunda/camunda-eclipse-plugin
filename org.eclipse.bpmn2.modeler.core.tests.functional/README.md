Jubula GUI Test for BPMN 2.0 Modeler for Eclipse
================================================

Install Jubula
--------------

Download and install the Jubula Standalone Edition [Jubula Project Site](http://www.eclipse.org/jubula/download.php).
For this project we are using Jubula-Version 1.3.0 with a Oracle 11g data base.
Import the project xml file `bpmn2_modeler_X.X.xml`. 


Building the AUT
----------------

Build a RCP application of the BPMN 2.0 Modeler.
You can change AUT working directory in Jubula. The default target folder is `\dev\Jubula\AUT\bpmn2_modeler\eclipse`.


Setup the AUT
-------------

As we are using absolute coordinates for our tests you have to adjust the Eclipse perspective of the AUT. 
In the folder `org.eclipse.e4.workbench` we provide a suitable Eclipse perspective called `Jubula Test`.
Replace the folder `org.eclipse.e4.workbench` in the AUT's `workspace/.metadata/.plugins` and use the `Jubula Test` perspective `Window --> Open Perspectiv --> Jubula Test`.

Please note that the perspective is modified for a screen resolution of 1366x768 pixel. If you are using a different resolution adjust the AUT's Properties Panel according to the following screen shot.

(https://raw.github.com/camunda/bpmn2-modeler/develop/org.eclipse.bpmn2.modeler.core.tests.functional/documentation/images/Eclipse_perspective.png)


Import template diagrams
------------------------

The folder `template diagrams` provides all necessary diagrams to run the Jubula tests.


