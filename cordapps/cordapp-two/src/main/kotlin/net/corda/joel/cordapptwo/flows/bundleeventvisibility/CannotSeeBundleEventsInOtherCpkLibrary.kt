package net.corda.joel.cordapptwo.flows.bundleeventvisibility

import net.corda.joel.cordapptwo.CordappTwoEventRecorderService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.joel.sharedlib.SHARED_LIB

@InitiatingFlow
@StartableByRPC
class CannotSeeBundleEventsInOtherCpkLibrary : Flow<Unit> {
    @CordaInject
    lateinit var cordappEventRecorderService: CordappTwoEventRecorderService

    override fun call() {
        if (cordappEventRecorderService.bundleEventSources.contains(SHARED_LIB)) {
            throw IllegalStateException("A CorDapp bundle could see bundle events in another CPK's library bundle.")
        }
    }
}