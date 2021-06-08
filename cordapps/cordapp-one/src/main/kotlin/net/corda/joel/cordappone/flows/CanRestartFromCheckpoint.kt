package net.corda.joel.cordappone.flows

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.corda.v5.application.flows.*
import net.corda.v5.application.flows.flowservices.FlowIdentity
import net.corda.v5.application.flows.flowservices.FlowMessaging
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.v5.base.annotations.Suspendable
import kotlin.system.exitProcess

@InitiatingFlow
@StartableByRPC
class CanRestartFromCheckpoint @JsonConstructor constructor(params: RpcStartFlowRequestParameters) : Flow<Unit> {

    companion object {
        private var shouldFail = false
    }

    private val setToFail = ObjectMapper()
        .readValue(params.parametersInJson, ObjectNode::class.java)["setToFail"]
        .toString()

    @CordaInject
    lateinit var flowIdentity: FlowIdentity

    @CordaInject
    lateinit var flowMessaging: FlowMessaging

    @Suspendable
    override fun call() {
        if (setToFail == "true") {
            shouldFail = true

        } else {
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
}