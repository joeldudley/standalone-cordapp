package net.corda.joel.sharedlib

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable

/** Used to check that flows in libraries cannot be started via RPC. */
@InitiatingFlow
@StartableByRPC
class FlowInLibrary : Flow<Unit> {
    @Suspendable
    override fun call() {
    }
}