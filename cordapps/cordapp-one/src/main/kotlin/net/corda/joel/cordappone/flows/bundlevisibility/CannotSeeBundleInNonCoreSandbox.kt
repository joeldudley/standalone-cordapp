package net.corda.joel.cordappone.flows.bundlevisibility

import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CannotSeeBundleInNonCoreSandbox @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val bundle = bundleContext.bundles.find {
                bundle -> bundle.symbolicName == "net.corda.node"
        }

        if (bundle != null) throw IllegalStateException("CorDapp could find non-core bundle.")
    }
}