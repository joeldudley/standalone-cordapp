package net.corda.joel.client

import net.corda.client.rpc.flow.FlowStarterRPCOps
import net.corda.client.rpc.flow.RpcFlowStatus
import net.corda.client.rpc.flow.RpcStartFlowRequest
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.RpcStartFlowRequestParameters
import net.corda.v5.httprpc.client.HttpRpcClient
import net.corda.v5.httprpc.client.config.HttpRpcClientConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

private const val HTTP_ADDRESS = "http://localhost:8888/api/v1/"
private const val HTTP_USERNAME = "user"
private const val HTTP_PASSWORD = "test"

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

    @Test
    fun sampleTest() {
        println(httpRpcOps.registeredFlows())
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
        return rpcFlowStartResponse.flowId.uuid.toString()
    }
}