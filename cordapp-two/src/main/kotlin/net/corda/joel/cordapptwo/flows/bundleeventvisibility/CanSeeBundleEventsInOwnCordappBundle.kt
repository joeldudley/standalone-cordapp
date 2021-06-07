package net.corda.joel.cordapptwo.flows.bundleeventvisibility

import net.corda.joel.cordapptwo.CordappTwoEventRecorderService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject

@InitiatingFlow
@StartableByRPC
class CanSeeBundleEventsInOwnCordappBundle : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappTwoEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.bundleEventSources.contains("com.template.cordapp-two")) {
            throw IllegalStateException("A CorDapp bundle could not see bundle events in its own CorDapp bundle.")
        }
    }
}