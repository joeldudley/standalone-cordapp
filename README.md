This project contains a set of CorDapps designed to test the Corda node running on OSGi.

# CorDapps

There are four CorDapps:

* `cordapp-one` and `cordapp-two`: These contain various flows and other classes that are useful in testing the 
  correct functioning of an OSGi Corda node
* `bad-cordapps:cordapp-has-cordapp-dependency-on-library`: This CorDapp should fail to load as it declares a 
  `cordapp` dependency on a module that is not a CorDapp
* `bad-cordapps:cordapp-imports-non-core-platform-bundle`: This CorDapp should fail to load as it tries to depend on a 
  non-core Corda platform bundle
* `bad-cordapps:cordapp-has-same-bundle-symbolic-name`: This CorDapp should fail to load as it shares a symbolic name 
  with CorDapp One
* `bad-cordapps:cordapp-has-same-contract`: This CorDapp should fail to load as it shares a fully-qualified contract 
  name with CorDapp One
* `bad-cordapps:cordapp-has-same-flow`: This CorDapp should fail to load as it shares a fully-qualified flow name with 
  CorDapp One

There are also two library modules:

* `shared-lib`: A library shared by `cordapp-one` and `cordapp-two` to test the isolation of libraries between 
  CorDapps
* `cordapp-one-lib`: A library that only `cordapp-one` depends on, to test that its classes are not visible to other 
  CorDapps

# Running the tests

## TestClient.kt tests

The majority of the tests are in `TestClient.kt`. These can be run as follows:

* Install the latest snapshot of Corda by cloning the Corda 5 repo and calling `./gradlew publishToMavenLocal`

* Deploy a node by running `./gradlew deployNodes`
  
* Run a node by running `cd build/nodes/NotaryNode` then `java -jar corda.jar`

  * The installation of the `bad-cordapps:*` is commented out because they cause the node to crash. These can be 
    re-enabled to test that they cause the node to fail at start-up

* Run the automated tests by running `./gradlew client:run`
  
  * Some tests are commented out because they cause the node to crash as part of normal execution. These can be 
    re-enabled to test specific behaviour

## Bad CorDapp tests

Re-enabling individual bad CorDapps in the `deployNodes` task (where they are currently commented out) allows you to 
test specific behaviours.

## Implicit tests

There are also some implicit tests, which "execute" just by turning on the node:

* CorDapp One contains a set of popular dependencies that are accessed in `DependencyImports.kt`. This allows us to 
  test that these popular dependencies do not clash with Corda's own packages
* CorDapp One and CorDapp Two both contain a non-flow class with the same fully-qualified name, 
  `net.corda.joel.duplicate.DuplicateNonFlowClass`. This allows us to test that two non-flow classes with the same 
  name can exist in two different CorDapps