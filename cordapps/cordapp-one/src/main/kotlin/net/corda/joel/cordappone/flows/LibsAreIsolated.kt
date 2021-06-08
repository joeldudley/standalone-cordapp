package net.corda.joel.cordappone.flows

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.joel.sharedlib.ClassWithModifiableStatic

@InitiatingFlow
@StartableByRPC
class LibsAreIsolated : Flow<Unit> {
    @Suspendable
    override fun call() {
        if (ClassWithModifiableStatic.modifiableStaticCounter != 0) {
            throw IllegalStateException("Static did not have the expected value.")
        }
    }
}