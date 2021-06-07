package net.corda.joel.cordapptwo.flows.bundleeventvisibility

import net.corda.joel.cordapptwo.CordappTwoEventRecorderService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject

@InitiatingFlow
@StartableByRPC
class CannotSeeBundleEventsInOtherCpkLibrary : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappTwoEventRecorderService

    override fun call() {
        if (cordappEventRecorderService.bundleEventSources.contains("shared-lib")) {
            throw IllegalStateException("A CorDapp bundle could see bundle events in another CPK's library bundle.")
        }
    }
}