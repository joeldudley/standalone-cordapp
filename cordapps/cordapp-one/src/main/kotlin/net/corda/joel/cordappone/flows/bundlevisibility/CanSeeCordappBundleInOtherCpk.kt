package net.corda.joel.cordappone.flows.bundlevisibility

import net.corda.joel.cordapptwo.flows.utility.IncrementSharedLibStatic
import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable

@InitiatingFlow
@StartableByRPC
class CanSeeCordappBundleInOtherCpk @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<String> {

    @Suspendable
    override fun call(): String {
        // TODO: Move to finding the bundle by name, as in `CanSeeLibraryInOwnCpk`
        return IncrementSharedLibStatic::class.java.name
    }
}