package net.corda.joel.cordappone.flows

import net.corda.v5.application.flows.*
import net.corda.v5.application.flows.flowservices.FlowIdentity
import net.corda.v5.application.flows.flowservices.FlowMessaging
import net.corda.v5.application.injection.CordaInject
import net.corda.v5.application.services.json.JsonMarshallingService
import net.corda.v5.application.services.json.parseJson
import net.corda.v5.base.annotations.Suspendable
import kotlin.system.exitProcess

@InitiatingFlow
@StartableByRPC
class CanRestartFromCheckpoint @JsonConstructor constructor(private val params: RpcStartFlowRequestParameters) :
    Flow<Unit> {

    companion object {
        // Conditioning the exit on a static variable means that the variable is reset to false after the node
        // restarts, avoiding it exiting in an infinite loop.
        private var shouldFail = false
    }

    @CordaInject
    lateinit var flowIdentity: FlowIdentity

    @CordaInject
    lateinit var flowMessaging: FlowMessaging

    @CordaInject
    lateinit var jsonMarshallingService: JsonMarshallingService

    @Suspendable
    override fun call() {
        val setToFail = jsonMarshallingService.parseJson<Map<String, Boolean>>(params.parametersInJson)["setToFail"]

        if (setToFail == true) {
            shouldFail = true
            return
        }

        println("Flow started.")

        val msg = "messageSentToCreateCheckpoint"
        val counterparty = flowIdentity.ourIdentity
        flowMessaging.initiateFlow(counterparty).send(msg)

        println("Message sent.")

        if (shouldFail) {
            println("Process will hang.")
            exitProcess(0)
        }

        println("Flow continued.")
    }
}