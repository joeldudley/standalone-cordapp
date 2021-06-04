package net.corda.joel.cordappone.flows

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import kotlin.system.exitProcess

@InitiatingFlow
@StartableByRPC
class CheckCanRestartFromCheckpoint(private val setToFail: Boolean = false) : FlowLogic<Unit>() {
    companion object {
        private var shouldFail = false
    }

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