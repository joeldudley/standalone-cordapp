package net.corda.joel.cordappone.flows.utility

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import kotlin.system.exitProcess

/**
 * Calling this flow with `setToFail` set to `true` sets a static flag in the class. If the flow is subsequently
 * called with `setToFail` set to `false`, it will cause the node to crash.
 *
 * Upon restarting, the class's static flag will reset to `false`, and the flow will complete as normal.
 */
@InitiatingFlow
@StartableByRPC
class KillNode @JsonConstructor constructor(params: RpcStartFlowRequestParameters) : Flow<Unit> {

    companion object {
        // If we don't condition the exit on some variable, then when the node restarts it will rerun the flow and exit
        // again.
        private var shouldFail = false
    }

    private val setToFail = ObjectMapper()
        .readValue(params.parametersInJson, ObjectNode::class.java)["setToFail"]
        .toString()

    @Suspendable
    override fun call() {
        if (setToFail == "true") {
            shouldFail = true
        } else {
            if (shouldFail) {
                println("Process will hang.")
                exitProcess(0)
            }
        }
    }
}