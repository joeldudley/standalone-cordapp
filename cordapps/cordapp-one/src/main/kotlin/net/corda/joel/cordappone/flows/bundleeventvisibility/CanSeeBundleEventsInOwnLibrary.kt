package net.corda.joel.cordappone.flows.bundleeventvisibility

import net.corda.joel.cordappone.CordappOneEventRecorderService
import net.corda.joel.sharedlib.SHARED_LIB
import net.corda.v5.application.flows.*
import net.corda.v5.application.injection.CordaInject

@InitiatingFlow
@StartableByRPC
class CanSeeBundleEventsInOwnLibrary @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @CordaInject
    lateinit var cordappEventRecorderService: CordappOneEventRecorderService

    override fun call() {
        if (!cordappEventRecorderService.bundleEventSources.contains(SHARED_LIB)) {
            throw IllegalStateException("A CorDapp bundle could not see bundle events in its own library bundle.")
        }
    }
}