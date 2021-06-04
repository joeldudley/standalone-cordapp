package net.corda.joel.client

import net.corda.client.rpc.CordaRPCClient
import net.corda.joel.cordappone.flows.*
import net.corda.joel.cordappone.flows.utility.KillNode
import net.corda.joel.cordappone.flows.utility.RegisterCordappService
import net.corda.joel.cordappone.flows.utility.RegisterLibraryService
import net.corda.joel.cordappone.flows.utility.SetIsolatedLibStatic
import net.corda.joel.cordapptwo.flows.*
import net.corda.v5.application.flows.Flow
import net.corda.v5.base.util.NetworkHostAndPort.Companion.parse
import java.io.File

private const val CPKS_DIRECTORY = "./build/nodes/NotaryNode/cpks"

fun main(args: Array<String>) {
    val testClient = TestClient()
    testClient.test()
    testClient.close()
}

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
        // TODO: Currently broken.
        // testCombinedTransactions()

        // Enabling these tests causes node to exit. Only one should be enabled at a time.
        // testRestoreFromCheckpoint()
        // testRebuildingOfCpkCache()
    }

    /**
     * CorDapp One and CorDapp two have a shared library, which contains `ClassWithModifiableStatic`. We check that
     * modifying a static in CorDapp One's copy of the shared class does not affect the same static in CorDapp Two's
     * copy.
     */
    private fun testStaticIsolation() {
        runFlowSync(SetIsolatedLibStatic::class.java, 99)
        runFlowSync(CheckIsolatedLibsFlow::class.java)
    }

    private fun testBundleVisibility() {
        runFlowSync(CheckCanSeeBundlesInCoreSandbox::class.java)
        runFlowSync(CheckCannotSeeBundlesInNonCoreSandbox::class.java)

        runFlowSync(CheckCanLoadClassInCoreSandbox::class.java)
        runFlowSync(CheckCannotLoadClassInNonCoreSandbox::class.java)

        runFlowSync(CheckCanSeeCordappBundleInOtherCpk::class.java)
        runFlowSync(CheckCannotSeeLibraryBundleInOtherCpk::class.java)
        runFlowSync(CheckCanSeeLibraryBundleInOwnCpk::class.java)
    }

    /**
     * We check that CorDapps can only see services registered by CorDapp bundles or their own library bundles.
     *
     * They should also be able to see services registered by the core platform bundles, but unfortunately we can't
     * test this, as there aren't any.
     */
    private fun testServiceVisibility() {
        runFlowSync(CheckCannotSeeServiceInNonCoreSandbox::class.java)

        // We register a service from the CorDapp bundle of CorDapp One. We check the service can be found from the
        // CorDapp bundle of CorDapp One and CorDapp Two.
        runFlowSync(RegisterCordappService::class.java)
        runFlowSync(CheckCanSeeServiceInOwnCpkCordappBundle::class.java)
        runFlowSync(CheckCanSeeServiceInOtherCpkCordappBundle::class.java)

        // We register a service from a library of CorDapp One. We check the service can be found from a CorDapp
        // bundle of CorDapp One, but not from a CorDapp bundle of CorDapp Two.
        runFlowSync(RegisterLibraryService::class.java)
        runFlowSync(CheckCanSeeServiceInOwnCpkLibrary::class.java)
        runFlowSync(CheckCannotSeeServiceInOtherCpkLibrary::class.java)
    }

    @Suppress("unused")
    private fun testCombinedTransactions() {
        runFlowSync(CheckCanBuildTxFromMultipleFlowsAndTheirLibs::class.java)
    }

    @Suppress("unused")
    private fun testRestoreFromCheckpoint() {
        // We set the node to exit when running the flow.
        val setToFail = true
        runFlowSync(CheckCanRestartFromCheckpoint::class.java, setToFail)
        // We run the flow.
        runFlowAsync(CheckCanRestartFromCheckpoint::class.java)
        // The node will crash. Once restarted, it should automatically complete the flow.
    }

    @Suppress("unused")
    private fun testRebuildingOfCpkCache() {
        // We set the node to exit when running the flow.
        val setToFail = true
        runFlowSync(KillNode::class.java, setToFail)
        runFlowAsync(KillNode::class.java)

        // The node will crash. Once restarted (after deleting its CPK directory, below), it should start
        // successfully.

        val cpksDirectory = File(CPKS_DIRECTORY)
        if (!cpksDirectory.exists()) throw IllegalStateException("Node CPKs directory does not exist.")
        val isDeleted = cpksDirectory.deleteRecursively()
        if (!isDeleted) throw IllegalStateException("Node CPKs directory could not be deleted.")
    }

    /** Closes the [cordaRpcOpsConnection]. */
    fun close() {
        cordaRpcOpsConnection.close()
    }

    /** Run [flowClass] with [args] and await the result. */
    private fun runFlowSync(flowClass: Class<out Flow<*>>, vararg args: Any?) {
        cordaRpcOpsProxy.startFlowDynamic(flowClass, *args).returnValue.get()
    }

    /** Run [flowClass] with [args] but do not await the result. */
    private fun runFlowAsync(flowClass: Class<out Flow<*>>, vararg args: Any?) {
        cordaRpcOpsProxy.startFlowDynamic(flowClass, *args)
    }
}