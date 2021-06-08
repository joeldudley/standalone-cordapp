package net.corda.joel.cordappone.flows

import net.corda.v5.base.annotations.Suspendable
import net.corda.joel.sharedlib.ClassWithModifiableStatic
import net.corda.v5.application.flows.*

@InitiatingFlow
@StartableByRPC
class LibsAreIsolated @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        if (ClassWithModifiableStatic.modifiableStaticCounter != 0) {
            throw IllegalStateException("Static did not have the expected value.")
        }
    }
}