package net.corda.joel.cordapptwo.flows.bundleeventvisibility

import net.corda.joel.cordapptwo.CordappTwoEventRecorderService
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.joel.sharedlib.SHARED_LIB
import net.corda.v5.application.flows.*

@InitiatingFlow
@StartableByRPC
class CannotSeeBundleEventsInOtherCpkLibrary @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @CordaInject
    lateinit var cordappEventRecorderService: CordappTwoEventRecorderService

    override fun call() {
        if (cordappEventRecorderService.bundleEventSources.contains(SHARED_LIB)) {
            throw IllegalStateException("A CorDapp bundle could see bundle events in another CPK's library bundle.")
        }
    }
}