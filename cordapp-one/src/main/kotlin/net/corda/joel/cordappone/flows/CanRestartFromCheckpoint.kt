package net.corda.joel.cordappone.flows

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.FlowIdentity
import net.corda.v5.application.flows.flowservices.FlowMessaging
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.v5.base.annotations.Suspendable
import kotlin.system.exitProcess

@InitiatingFlow
@StartableByRPC
class CanRestartFromCheckpoint(private val setToFail: Boolean = false) : Flow<Unit> {
    companion object {
        private var shouldFail = false
    }

    @CordaInject
    lateinit var flowIdentity: FlowIdentity

    @CordaInject
    lateinit var flowMessaging: FlowMessaging

    @Suspendable
    override fun call() {
        if (setToFail) {
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