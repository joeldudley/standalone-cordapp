package net.corda.joel.cordappone.flows.serviceeventvisibility

import net.corda.joel.cordappone.CordappOneEventRecorderService
import net.corda.joel.sharedlib.CORDAPP_ONE
import net.corda.v5.application.flows.*
import net.corda.v5.application.injection.CordaInject

@InitiatingFlow
@StartableByRPC
class CanSeeServiceEventsInOwnCordappBundle @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @CordaInject
    lateinit var cordappEventRecorderService: CordappOneEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.serviceEventSources.contains(CORDAPP_ONE)) {
            throw IllegalStateException("A CorDapp bundle could not see service events in its own CorDapp bundle.")
        }
    }
}