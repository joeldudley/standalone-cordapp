package net.corda.joel.cordappone.flows.serviceeventvisibility

import net.corda.joel.cordappone.CordappOneEventRecorderService
import net.corda.joel.sharedlib.CORDAPP_ONE_LIB
import net.corda.v5.application.flows.*
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject

@InitiatingFlow
@StartableByRPC
class CanSeeServiceEventsInOwnLibrary @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @CordaInject
    lateinit var cordappEventRecorderService: CordappOneEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.serviceEventSources.contains(CORDAPP_ONE_LIB)) {
            throw IllegalStateException("A CorDapp bundle could not see service events in its own library bundle.")
        }
    }
}