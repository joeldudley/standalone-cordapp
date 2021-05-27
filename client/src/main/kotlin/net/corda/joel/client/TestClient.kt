package net.corda.joel.client

import net.corda.client.rpc.CordaRPCClient
import net.corda.joel.cordapp.*
import net.corda.joel.cordapp2.CheckCanSeeCordappBundleInOtherCpk
import net.corda.joel.cordapp2.CheckCannotSeeLibraryBundleInOtherCpk
import net.corda.joel.cordapp2.CheckCannotSeeServiceInOtherCpkLibrary
import net.corda.joel.cordapp2.CheckIsolatedLibsFlow
import net.corda.v5.base.util.NetworkHostAndPort.Companion.parse

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
        testLibraryIsolation()
        testBundleVisibility()
        testServiceVisibility()

        // Enabling this causes node to crash as part of a test.
        testRestoreFromCheckpoint()
    }

    private fun testLibraryIsolation() {
        // Running the flow sets CorDapp One's library static to 99.
        cordaRpcOpsProxy.startFlowDynamic(SetIsolatedLibStatic::class.java, 99).returnValue.get()
        // We check that CorDapp Two's library static is still 0.
        cordaRpcOpsProxy.startFlowDynamic(CheckIsolatedLibsFlow::class.java).returnValue.get()
    }

    private fun testBundleVisibility() {
        cordaRpcOpsProxy.startFlowDynamic(CheckCanSeeBundlesInCoreSandbox::class.java)
        cordaRpcOpsProxy.startFlowDynamic(CheckCannotSeeBundlesInNonCoreSandbox::class.java)

        cordaRpcOpsProxy.startFlowDynamic(CheckCanSeeCordappBundleInOtherCpk::class.java)

        cordaRpcOpsProxy.startFlowDynamic(CheckCanSeeLibraryBundleInOwnCpk::class.java)
        cordaRpcOpsProxy.startFlowDynamic(CheckCannotSeeLibraryBundleInOtherCpk::class.java)
    }

    private fun testServiceVisibility() {
        cordaRpcOpsProxy.startFlowDynamic(CheckCannotSeeServiceInNonCoreSandbox::class.java)

        // We register a service from a library of CorDapp One.
        cordaRpcOpsProxy.startFlowDynamic(RegisterLibraryService::class.java)
        // We check the service can be found from a CorDapp bundle of CorDapp One.
        cordaRpcOpsProxy.startFlowDynamic(CheckCanSeeServiceInOwnCpkLibrary::class.java)
        // ...but not from a CorDapp bundle of CorDapp Two.
        cordaRpcOpsProxy.startFlowDynamic(CheckCannotSeeServiceInOtherCpkLibrary::class.java)
    }

    private fun testRestoreFromCheckpoint() {
        // We set the flow to fail.
        val setToFail = true
        cordaRpcOpsProxy.startFlowDynamic(CheckCanRestartFromCheckpoint::class.java, setToFail)
        // We run the flow.
        cordaRpcOpsProxy.startFlowDynamic(CheckCanRestartFromCheckpoint::class.java)
    }

    fun close() {
        cordaRpcOpsConnection.close()
    }
}