* Deploy a node by running `./gradlew deployNodes`
  
* Run a node by running `cd build/nodes/NotaryNode` then `java -jar corda.jar`
  
* Run the automated tests by running `./gradlew client:run`
  
  * Some tests are commented out because they cause the node to crash. These can be re-enabled to test specific 
    behaviour

* The `bad-cordapps` folder contains CorDapp that fail for various reasons. You can test them by assembling them and 
  dropping them into the node's `cordapps` folder