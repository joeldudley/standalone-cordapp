package net.corda.joel.client

import net.corda.client.rpc.PermissionException
import net.corda.client.rpc.flow.FlowStarterRPCOps
import net.corda.client.rpc.flow.RpcFlowStatus
import net.corda.client.rpc.flow.RpcStartFlowRequest
import net.corda.joel.cordappone.flows.CanBuildTxFromMultipleCordappsAndTheirLibs
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
import net.corda.joel.cordapptwo.flows.utility.IncrementSharedLibStatic
import net.corda.joel.sharedlib.FlowInLibrary
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.RpcStartFlowRequestParameters
import net.corda.v5.httprpc.client.HttpRpcClient
import net.corda.v5.httprpc.client.config.HttpRpcClientConfig
import org.junit.jupiter.api.*
import java.io.File
import java.util.*

private const val HTTP_ADDRESS = "http://localhost:8888/api/v1/"
private const val HTTP_USERNAME = "user"
private const val HTTP_PASSWORD = "test"
private const val CPKS_DIRECTORY = "../build/nodes/NotaryNode/cpks"

class ClientTests {
    private lateinit var httpRpcClient: HttpRpcClient<FlowStarterRPCOps>
    private lateinit var httpRpcOps: FlowStarterRPCOps

    /** Creates a [HttpRpcClient] for the node and retrieves the [FlowStarterRPCOps]. */
    @BeforeEach
    fun setUp() {
        val httpConfig = HttpRpcClientConfig().username(HTTP_USERNAME).password(HTTP_PASSWORD)
        httpRpcClient = HttpRpcClient(HTTP_ADDRESS, FlowStarterRPCOps::class.java, httpConfig)
        httpRpcOps = httpRpcClient.start().proxy
    }

    /** Closes down the [httpRpcClient]. */
    @AfterEach
    fun tearDown() {
        httpRpcClient.close()
    }

    /** We check that CorDapps receive separate copies of shared libraries. */
    @Test
    fun testStaticsIsolation() {
        // We increment the value of a static in CorDapp Two's copy of a shared library.
        runFlowSync(IncrementSharedLibStatic::class.java)
        // We check that the value of the static is unchanged in CorDapp One's copy of the same library.
        runFlowSync(LibsAreIsolated::class.java)
    }

    /** We check that bundles can only see CorDapp bundles, their own library bundles, and core platform bundles. */
    @Test
    fun testBundleVisibility() {
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
    @Test
    fun testServiceVisibility() {
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
    @Test
    fun testTransactions() {
         runFlowSync(CanBuildTxFromMultipleCordappsAndTheirLibs::class.java)
    }

    /** We test that flows in libraries cannot be started via RPC. */
    @Test
    fun testFlows() {
        assertThrows<PermissionException> {
            runFlowSync(FlowInLibrary::class.java)
        }
    }

    /** We check that a node can restart successfully from a checkpoint. */
    @Disabled("Enabling this test causes node to exit as part of normal execution.")
    @Test
    fun testRestartFromCheckpoint() {
        // We set the node to exit when running the flow.
        runFlowSync(CanRestartFromCheckpoint::class.java, """{"setToFail": true}""")
        // We run the flow.
        runFlowAsync(CanRestartFromCheckpoint::class.java, """{"setToFail": false}""")
        // The node will crash. Once restarted, the flow should automatically complete.
    }

    /** We check that a node can successfully rebuild its CPK cache if needed. */
    @Disabled("Enabling this test causes node to exit as part of normal execution.")
    @Test
    fun testRebuildingOfCpkCache() {
        // We set the node to exit when running the flow.
        runFlowSync(KillNode::class.java, """{"setToFail": true}""")
        runFlowAsync(KillNode::class.java, """{"setToFail": false}""")

        // The node will crash. Once restarted (after deleting its CPK directory, below), it should start
        // successfully.

        val cpksDirectory = File(CPKS_DIRECTORY)
        if (!cpksDirectory.exists()) throw IllegalStateException("Node CPKs directory does not exist.")
        val isDeleted = cpksDirectory.deleteRecursively()
        if (!isDeleted) throw IllegalStateException("Node CPKs directory could not be deleted.")
    }

    /** Run [flowClass] with [jsonArgs] and await the result. */
    private fun runFlowSync(flowClass: Class<out Flow<*>>, jsonArgs: String = "") {
        val flowUuid = runFlowAsync(flowClass, jsonArgs)
        while (httpRpcOps.getFlowOutcome(flowUuid).status != RpcFlowStatus.COMPLETED) {
            Thread.sleep(100)
        }
    }

    /** Run [flowClass] with [jsonArgs] and return the flow's UUID as a string. */
    private fun runFlowAsync(flowClass: Class<out Flow<*>>, jsonArgs: String = ""): String {
        val rpcStartFlowRequestParameters = RpcStartFlowRequestParameters(jsonArgs)
        val clientId = "client-${UUID.randomUUID()}"
        val rpcStartFlowRequest = RpcStartFlowRequest(flowClass.name, clientId, rpcStartFlowRequestParameters)
        val rpcFlowStartResponse = httpRpcOps.startFlow(rpcStartFlowRequest)
        return rpcFlowStartResponse.stateMachineRunId.uuid.toString()
    }
}