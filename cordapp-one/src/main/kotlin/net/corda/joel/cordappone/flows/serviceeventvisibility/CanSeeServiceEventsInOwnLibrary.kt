package net.corda.joel.cordappone.flows.serviceeventvisibility

import net.corda.joel.cordappone.CordappOneEventRecorderService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import java.lang.IllegalStateException

@InitiatingFlow
@StartableByRPC
class CanSeeServiceEventsInOwnLibrary : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappOneEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.serviceEventSources.contains("shared-lib")) {
            throw IllegalStateException("A CorDapp bundle could not see service events in its own library bundle.")
        }
    }
}