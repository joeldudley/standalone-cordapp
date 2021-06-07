package net.corda.joel.cordapptwo.flows.serviceeventvisibility

import net.corda.joel.cordapptwo.CordappTwoEventRecorderService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject

@InitiatingFlow
@StartableByRPC
class CanSeeServiceEventsInOtherCpkCordappBundle : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappTwoEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.serviceEventSources.contains("com.template.cordapp-one")) {
            throw IllegalStateException("A CorDapp bundle could not see service events in another CPK's CorDapp " +
                    "bundle.")
        }
    }
}