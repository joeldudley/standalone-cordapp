package net.corda.joel.sharedlib

import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable

/** Used to check that flows in libraries cannot be started via RPC. */
@InitiatingFlow
@StartableByRPC
class FlowInLibrary @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
    }
}