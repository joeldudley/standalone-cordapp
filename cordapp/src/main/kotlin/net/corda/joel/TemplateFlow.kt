package net.corda.joel

import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable

@InitiatingFlow
@StartableByRPC
class TemplateFlow @JsonConstructor constructor(private val params: RpcStartFlowRequestParameters) : Flow<Unit> {

    @Suspendable
    override fun call() {
        println("Doing stuff.")
    }
}