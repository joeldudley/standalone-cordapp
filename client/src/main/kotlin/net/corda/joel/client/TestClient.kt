package net.corda.joel.client

import net.corda.client.rpc.CordaRPCClient
import net.corda.joel.cordappone.flows.CanRestartFromCheckpoint
import net.corda.joel.cordappone.flows.LibsAreIsolated
import net.corda.joel.cordappone.flows.bundleeventvisibility.CanSeeBundleEventsInOtherCpkCordappBundle
import net.corda.joel.cordappone.flows.bundleeventvisibility.CanSeeBundleEventsInOwnLibrary
import net.corda.joel.cordappone.flows.bundlevisibility.*
import net.corda.joel.cordappone.flows.serviceeventvisibility.CanSeeServiceEventsInOwnCordappBundle
import net.corda.joel.cordappone.flows.serviceeventvisibility.CanSeeServiceEventsInOwnLibrary
import net.corda.joel.cordappone.flows.servicevisibility.CanSeeServiceInOwnCordappBundle
import net.corda.joel.cordappone.flows.servicevisibility.CanSeeServiceInOwnLibrary
import net.corda.joel.cordappone.flows.servicevisibility.CannotSeeServiceInNonCoreSandbox
import net.corda.joel.cordappone.flows.utility.GenerateBundleEvents
import net.corda.joel.cordappone.flows.utility.KillNode
import net.corda.joel.cordappone.flows.utility.RegisterCordappAndLibraryServices
import net.corda.joel.cordapptwo.flows.bundleeventvisibility.CannotSeeBundleEventsInOtherCpkLibrary
import net.corda.joel.cordapptwo.flows.bundlevisibility.CannotSeeLibraryBundleInOtherCpk
import net.corda.joel.cordapptwo.flows.serviceeventvisibility.CanSeeServiceEventsInOtherCpkCordappBundle
import net.corda.joel.cordapptwo.flows.serviceeventvisibility.CannotSeeServiceEventsInOtherCpkLibrary
import net.corda.joel.cordapptwo.flows.servicevisibility.CanSeeServiceInOtherCpkCordappBundle
import net.corda.joel.cordapptwo.flows.servicevisibility.CannotSeeServiceInOtherCpkLibrary
import net.corda.joel.cordapptwo.flows.utility.SetSharedLibStatic
import net.corda.joel.sharedlib.FlowInLibrary
import net.corda.v5.application.flows.Flow
import net.corda.v5.base.util.NetworkHostAndPort.Companion.parse
import java.io.File

private const val CPKS_DIRECTORY = "./build/nodes/NotaryNode/cpks"

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
        testStaticsIsolation()
        testBundleVisibility()
        testServiceVisibility()
        testTransactions()
        testFlows()

        // Enabling these tests causes node to exit. Only one should be enabled at a time.
        // testRestartFromCheckpoint()
        // testRebuildingOfCpkCache()
    }

    /** We check that CorDapps receive separate copies of shared libraries. */
    private fun testStaticsIsolation() {
        // We set the value of a static in CorDapp Two's copy of a shared library.
        runFlowSync(SetSharedLibStatic::class.java, 99)
        // We check that the value of the static is unchanged in CorDapp One's copy of the same library.
        runFlowSync(LibsAreIsolated::class.java)
    }

    /** We check that bundles can only see CorDapp bundles, their own library bundles, and core platform bundles. */
    private fun testBundleVisibility() {
        runFlowSync(CanSeeBundleInCoreSandbox::class.java)
        runFlowSync(CannotSeeBundleInNonCoreSandbox::class.java)

        runFlowSync(CanLoadClassInCoreSandbox::class.java)
        runFlowSync(CannotLoadClassInNonCoreSandbox::class.java)

        runFlowSync(CanSeeCordappBundleInOtherCpk::class.java)
        runFlowSync(CanSeeLibraryInOwnCpk::class.java)
        runFlowSync(CannotSeeLibraryBundleInOtherCpk::class.java)

        // We generate two bundle events by restarting one of CorDapp One's library bundles and another CorDapp's
        // CorDapp bundle. We check that the first bundle event was only visible to CorDapp One, but the second was
        // visible to both CorDapp One and CorDapp Two.
        runFlowSync(GenerateBundleEvents::class.java)
        runFlowSync(CanSeeBundleEventsInOwnLibrary::class.java)
        runFlowSync(CannotSeeBundleEventsInOtherCpkLibrary::class.java)
        runFlowSync(CanSeeBundleEventsInOtherCpkCordappBundle::class.java)
    }

    /**
     * We check that CorDapps can only see services registered by CorDapp bundles or their own library bundles.
     *
     * They should also be able to see services registered by the core platform bundles, and not see services
     * registered by non-core platform bundles. Unfortunately, we can't test this, as there aren't any.
     */
    private fun testServiceVisibility() {
        // We register services from the CorDapp bundle of CorDapp One and a library bundle of CorDapp One.
        runFlowSync(RegisterCordappAndLibraryServices::class.java)

        runFlowSync(CannotSeeServiceInNonCoreSandbox::class.java)
        runFlowSync(CanSeeServiceInOwnCordappBundle::class.java)
        runFlowSync(CanSeeServiceInOtherCpkCordappBundle::class.java)
        runFlowSync(CanSeeServiceInOwnLibrary::class.java)
        runFlowSync(CannotSeeServiceInOtherCpkLibrary::class.java)

        runFlowSync(CanSeeServiceEventsInOwnCordappBundle::class.java)
        runFlowSync(CanSeeServiceEventsInOwnLibrary::class.java)
        runFlowSync(CanSeeServiceEventsInOtherCpkCordappBundle::class.java)
        runFlowSync(CannotSeeServiceEventsInOtherCpkLibrary::class.java)
    }

    /** We test that transactions containing classes from multiple CorDapp and library bundles can be committed. */
    private fun testTransactions() {
        // TODO - Currently broken.
        // runFlowSync(CanBuildTxFromMultipleCordappsAndTheirLibs::class.java)
    }

    /** We test that flows in libraries cannot be started via RPC. */
    private fun testFlows() {
        try {
            runFlowSync(FlowInLibrary::class.java)
        } catch (e: Exception) {
            return
        }
        throw IllegalArgumentException("Starting library flow via RPC did not throw an exception.")
    }

    /** We check that a node can restart successfully from a checkpoint. */
    @Suppress("unused")
    private fun testRestartFromCheckpoint() {
        // We set the node to exit when running the flow.
        val setToFail = true
        runFlowSync(CanRestartFromCheckpoint::class.java, setToFail)
        // We run the flow.
        runFlowAsync(CanRestartFromCheckpoint::class.java)
        // The node will crash. Once restarted, the flow should automatically complete.
    }

    /** We check that a node can successfully rebuild its CPK cache if needed. */
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