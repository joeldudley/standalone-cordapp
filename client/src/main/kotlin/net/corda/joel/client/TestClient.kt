package net.corda.joel.client

import net.corda.client.rpc.CordaRPCClient
import net.corda.joel.cordapp2.CheckCanSeeCordappBundleInOtherCpk
import net.corda.joel.cordapp2.CheckCannotSeeLibraryBundleInOtherCpk
import net.corda.joel.cordapp2.CheckIsolatedLibsFlow
import net.corda.joel.cordappone.*
import net.corda.v5.application.flows.Flow
import net.corda.v5.base.util.NetworkHostAndPort.Companion.parse
import java.io.File

private const val CPKS_DIRECTORY = "/Users/joeldudley/Desktop/cordapp-template-kotlin/build/nodes/NotaryNode/cpks"

fun main(args: Array<String>) {
    val testClient = TestClient()
    testClient.test()
    testClient.close()
}

// TODO - Add test of building and serialising a transaction containing elements from both CorDapps and their libraries.
// TODO - Move out of dev mode.

class TestClient {
    private val address = parse("localhost:10003")
    private val username = "user"
    private val password = "test"

    private val cordaRpcOpsClient = CordaRPCClient(address)
    private val cordaRpcOpsConnection = cordaRpcOpsClient.start(username, password)
    private val cordaRpcOpsProxy = cordaRpcOpsConnection.proxy

    fun test() {
        testStaticIsolation()
        testBundleVisibility()
        testServiceVisibility()

        // Enabling this causes node to exit as part of a test.
        // Currently fails due to a bug - see CORE-1497.
        // testRestoreFromCheckpoint()

        // Enabling this causes node to exit as part of a test.
        // testRebuildingOfCpkCache()
    }

    private fun testStaticIsolation() {
        // Running the flow sets CorDapp One's library static to 99.
        runFlowSync(SetIsolatedLibStatic::class.java, 99)
        // We check that CorDapp Two's library static is still 0.
        runFlowSync(CheckIsolatedLibsFlow::class.java)
    }

    private fun testBundleVisibility() {
        runFlowSync(CheckCanSeeBundlesInCoreSandbox::class.java)
        runFlowSync(CheckCannotSeeBundlesInNonCoreSandbox::class.java)

        runFlowSync(CheckCanSeeCordappBundleInOtherCpk::class.java)

        runFlowSync(CheckCanSeeLibraryBundleInOwnCpk::class.java)
        runFlowSync(CheckCannotSeeLibraryBundleInOtherCpk::class.java)

        runFlowSync(CheckCanLoadClassInCoreSandbox::class.java)
    }

    private fun testServiceVisibility() {
        // TODO: Broken.
        // runFlowSync(CheckCannotSeeServiceInNonCoreSandbox::class.java)

        // We register a service from a library of CorDapp One.
        runFlowSync(RegisterLibraryService::class.java)
        // We check the service can be found from a CorDapp bundle of CorDapp One.
        runFlowSync(CheckCanSeeServiceInOwnCpkLibrary::class.java)
        // ...but not from a CorDapp bundle of CorDapp Two.
        // TODO: Broken.
        // runFlowSync(CheckCannotSeeServiceInOtherCpkLibrary::class.java)
    }

    private fun testRestoreFromCheckpoint() {
        // We set the node to exit when running the flow.
        val setToFail = true
        runFlowSync(CheckCanRestartFromCheckpoint::class.java, setToFail)
        // We run the flow.
        runFlowAsync(CheckCanRestartFromCheckpoint::class.java)
        // The node will crash. Once restarted, it should automatically complete the flow.
    }

    private fun testRebuildingOfCpkCache() {
        // We set the node to exit when running the flow.
        val setToFail = true
        runFlowSync(KillNode::class.java, setToFail)
        runFlowAsync(KillNode::class.java)

        // The node will crash. Once restarted (after its CPK directory are deleted, below), it should start
        // successfully.

        val cpksDirectory = File(CPKS_DIRECTORY)
        if (!cpksDirectory.exists()) throw IllegalStateException("Node CPKs directory does not exist.")
        val isDeleted = cpksDirectory.deleteRecursively()
        if (!isDeleted) throw IllegalStateException("Node CPKs directory could not be deleted.")
    }

    fun close() {
        cordaRpcOpsConnection.close()
    }

    private fun runFlowSync(flowClass: Class<out Flow<*>>, vararg args: Any?) {
        cordaRpcOpsProxy.startFlowDynamic(flowClass, *args).returnValue.get()
    }

    private fun runFlowAsync(flowClass: Class<out Flow<*>>, vararg args: Any?) {
        cordaRpcOpsProxy.startFlowDynamic(flowClass, *args)
    }
}