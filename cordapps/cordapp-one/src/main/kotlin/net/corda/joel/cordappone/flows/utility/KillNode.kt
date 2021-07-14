package net.corda.joel.cordappone.flows.utility

import net.corda.v5.application.flows.*
import net.corda.v5.application.injection.CordaInject
import net.corda.v5.application.services.json.JsonMarshallingService
import net.corda.v5.application.services.json.parseJson
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
class KillNode @JsonConstructor constructor(private val params: RpcStartFlowRequestParameters) : Flow<Unit> {

    companion object {
        // Conditioning the exit on a static variable means that the variable is reset to false after the node
        // restarts, avoiding it exiting in an infinite loop.
        private var shouldFail = false
    }

    @CordaInject
    lateinit var jsonMarshallingService: JsonMarshallingService

    @Suspendable
    override fun call() {
        val setToFail = jsonMarshallingService.parseJson<Map<String, Boolean>>(params.parametersInJson)["setToFail"]

        if (setToFail == true) {
            shouldFail = true
            return
        }

        if (shouldFail) {
            println("Process will hang.")
            exitProcess(0)
        }
    }
}