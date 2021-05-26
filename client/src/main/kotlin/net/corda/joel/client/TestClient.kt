package net.corda.joel.client

import net.corda.client.rpc.CordaRPCClient
import net.corda.joel.cordapp.CheckCanSeeBundlesInCoreSandbox
import net.corda.joel.cordapp.CheckCannotSeeBundlesInNonCoreSandbox
import net.corda.joel.cordapp.CheckIsolatedLibsFlow1
import net.corda.joel.cordapp.CheckServiceRegistryEmpty
import net.corda.joel.cordapp2.CheckIsolatedLibsFlow2
import net.corda.v5.base.util.NetworkHostAndPort.Companion.parse
import java.lang.IllegalStateException

fun main(args: Array<String>) {
    val testClient = TestClient()
    testClient.test()
    testClient.close()
}

class TestClient {
    private val address = parse("localhost:10003")
    private val username = "user"
    private val password = "test"

    private val cordaRpcOpsClient = CordaRPCClient(address)
    private val cordaRpcOpsConnection = cordaRpcOpsClient.start(username, password)
    private val cordaRpcOpsProxy = cordaRpcOpsConnection.proxy

    fun test() {
        // Running the flow sets the first CorDapp's static to 99.
        cordaRpcOpsProxy.startFlowDynamic(CheckIsolatedLibsFlow1::class.java).returnValue.get()
        // We check that the second CorDapp's static is still 0.
        if (cordaRpcOpsProxy.startFlowDynamic(CheckIsolatedLibsFlow1::class.java).returnValue.get() != 0) {
            throw IllegalStateException("CorDapp library static was not correct isolated.")
        }
        // We run the flows again to reset both statics to zero.
        cordaRpcOpsProxy.startFlowDynamic(CheckIsolatedLibsFlow1::class.java).returnValue.get()
        cordaRpcOpsProxy.startFlowDynamic(CheckIsolatedLibsFlow1::class.java).returnValue.get()

        cordaRpcOpsProxy.startFlowDynamic(CheckCanSeeBundlesInCoreSandbox::class.java)
        cordaRpcOpsProxy.startFlowDynamic(CheckCannotSeeBundlesInNonCoreSandbox::class.java)
        cordaRpcOpsProxy.startFlowDynamic(CheckServiceRegistryEmpty::class.java)
    }

    fun close() {
        cordaRpcOpsConnection.close()
    }
}