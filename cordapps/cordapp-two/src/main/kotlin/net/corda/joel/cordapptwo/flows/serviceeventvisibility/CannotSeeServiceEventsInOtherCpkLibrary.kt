package net.corda.joel.cordapptwo.flows.serviceeventvisibility

import net.corda.joel.cordapptwo.CordappTwoEventRecorderService
import net.corda.joel.sharedlib.CORDAPP_ONE_LIB
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject

@InitiatingFlow
@StartableByRPC
class CannotSeeServiceEventsInOtherCpkLibrary : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappTwoEventRecorderService

    override fun call() {
        if (cordappEventRecorderService.serviceEventSources.contains(CORDAPP_ONE_LIB)) {
            throw IllegalStateException("A CorDapp bundle could see service events in another CPK's library bundle.")
        }
    }
}