This project contains a set of CorDapps designed to test the Corda node running on OSGi.

#Contents

There are four CorDapps:

* `cordapp-one` and `cordapp-two`: These contain various flows and other classes that are useful in testing the 
  correct functioning of an OSGi Corda node
* `bad-cordapps:cordapp-has-cordapp-dependency-on-library`: This CorDapp should fail to load as it declares a 
  `cordapp` dependency on a module that is not a CorDapp
* `bad-cordapps:cordapp-imports-non-core-platform-bundle`: This CorDapp should fail to load as it tries to depend on a 
  non-core Corda platform bundle

There are also two library modules:

* `shared-lib`: A library shared by `cordapp-one` and `cordapp-two` to test the isolation of libraries between 
  CorDapps
* `cordapp-one-lib`: A library that only `cordapp-one` depends on, to test that its classes are not visible to other 
  CorDapps

#Steps to run the tests:

* Install the latest snapshot of Corda by cloning the Corda 5 repo and calling `./gradlew publishToMavenLocal`

* Deploy a node by running `./gradlew deployNodes`
  
* Run a node by running `cd build/nodes/NotaryNode` then `java -jar corda.jar`

  * The installation of the `bad-cordapps:*` is commented out because they cause the node to crash. These can be 
    re-enabled to test that they cause the node to fail at start-up
  
* Run the automated tests by running `./gradlew client:run`
  
  * Some tests are commented out because they cause the node to crash as part of normal execution. These can be 
    re-enabled to test specific behaviour