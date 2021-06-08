package net.corda.joel.cordapptwo.flows.utility

import net.corda.joel.sharedlib.ClassWithModifiableStatic
import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable

/**
 * Sets a static in a library class shared with CorDapp One.
 */
@InitiatingFlow
@StartableByRPC
class IncrementSharedLibStatic @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        ClassWithModifiableStatic.modifiableStaticCounter++
    }
}