package net.corda.joel.cordapptwo.flows

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
//import net.corda.v5.ledger.services.NotaryAwareNetworkMapCache
import net.corda.v5.legacyapi.flows.FlowLogic
import net.joel.sharedlib.ClassWithModifiableStatic

@InitiatingFlow
@StartableByRPC
class CheckLibsAreIsolated : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        if (ClassWithModifiableStatic.modifiableStaticCounter != 0)
            throw IllegalStateException("Static did not have the expected value.")
    }
}