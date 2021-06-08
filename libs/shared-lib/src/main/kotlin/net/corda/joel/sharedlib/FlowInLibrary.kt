package net.corda.joel.sharedlib

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable

/** Used to check that flows in libraries are not made available to the node user. */
@InitiatingFlow
@StartableByRPC
class FlowInLibrary: Flow<Unit> {
    @Suspendable
    override fun call() {
        println("Flow in library called.")
    }
}