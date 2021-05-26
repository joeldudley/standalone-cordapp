package net.corda.joel.client

import net.corda.client.rpc.CordaRPCClient
import net.corda.joel.cordapp.CheckCanSeeBundlesInCoreSandbox
import net.corda.joel.cordapp.CheckCannotSeeBundlesInNonCoreSandbox
import net.corda.joel.cordapp.CheckIsolatedLibsFlow1
import net.corda.joel.cordapp.CheckServiceRegistryEmpty
import net.corda.joel.cordapp2.CheckIsolatedLibsFlow2
import net.corda.v5.base.util.NetworkHostAndPort.Companion.parse

fun main() {
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
        // Both flows import the same library. Each flow should have its own copy of the library, and thus see the
        // counter increment from zero.
        (0..5).forEach { x -> assert(x == cordaRpcOpsProxy.startFlowDynamic(CheckIsolatedLibsFlow1::class.java).returnValue.get()) }
        (0..5).forEach { x -> assert(x == cordaRpcOpsProxy.startFlowDynamic(CheckIsolatedLibsFlow2::class.java).returnValue.get()) }

        cordaRpcOpsProxy.startFlowDynamic(CheckCanSeeBundlesInCoreSandbox::class.java)
        cordaRpcOpsProxy.startFlowDynamic(CheckCannotSeeBundlesInNonCoreSandbox::class.java)
        cordaRpcOpsProxy.startFlowDynamic(CheckServiceRegistryEmpty::class.java)
    }

    fun close() {
        cordaRpcOpsConnection.close()
    }
}