package net.corda.joel.cordapptwo.flows.serviceeventvisibility

import net.corda.joel.cordapptwo.CordappTwoEventRecorderService
import net.corda.joel.sharedlib.CORDAPP_ONE_LIB
import net.corda.v5.application.flows.*
import net.corda.v5.application.injection.CordaInject

@InitiatingFlow
@StartableByRPC
class CannotSeeServiceEventsInOtherCpkLibrary @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @CordaInject
    lateinit var cordappEventRecorderService: CordappTwoEventRecorderService

    override fun call() {
        if (cordappEventRecorderService.serviceEventSources.contains(CORDAPP_ONE_LIB)) {
            throw IllegalStateException("A CorDapp bundle could see service events in another CPK's library bundle.")
        }
    }
}