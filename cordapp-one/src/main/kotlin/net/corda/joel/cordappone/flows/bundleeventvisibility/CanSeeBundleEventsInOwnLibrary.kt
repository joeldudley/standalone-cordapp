package net.corda.joel.cordappone.flows.bundleeventvisibility

import net.corda.joel.cordappone.CordappOneEventRecorderService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import java.lang.IllegalStateException

@InitiatingFlow
@StartableByRPC
class CanSeeBundleEventsInOwnLibrary : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappOneEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.bundleEventSources.contains("shared-lib")) {
            throw IllegalStateException("A CorDapp bundle could not see bundle events in its own library bundle.")
        }
    }
}