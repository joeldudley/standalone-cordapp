package net.corda.joel.cordappone.flows.bundleeventvisibility

import net.corda.joel.cordappone.CordappOneEventRecorderService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject

@InitiatingFlow
@StartableByRPC
class CanSeeBundleEventsInOtherCpkCordappBundle : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappOneEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.bundleEventSources.contains("com.template.cordapp-two")) {
            throw IllegalStateException("A CorDapp bundle could not see bundle events in another CPK's CorDapp " +
                    "bundle.")
        }
    }
}