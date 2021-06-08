package net.corda.joel.cordapptwo.flows.bundlevisibility

import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CannotSeeLibraryBundleInOtherCpk @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val bundle = bundleContext
            .bundles
            .find { bundle -> bundle.symbolicName == "cordapp-one-lib" }
        if (bundle != null) throw IllegalStateException("CorDapp could find library bundle in other CPK.")
    }
}