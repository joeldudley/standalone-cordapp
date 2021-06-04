package net.corda.joel.cordappone.flows.utility

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import kotlin.system.exitProcess

@InitiatingFlow
@StartableByRPC
class KillNode(private val setToFail: Boolean = false) : FlowLogic<Unit>() {
    companion object {
        // If we don't condition the exit on some variable, then when the node restarts it will rerun the flow and exit
        // again.
        private var shouldFail = false
    }

    @Suspendable
    override fun call() {
        if (setToFail) {
            shouldFail = true
        } else {
            if (shouldFail) {
                println("Process will hang.")
                exitProcess(0)
            }
        }
    }
}