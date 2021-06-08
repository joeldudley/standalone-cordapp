package net.corda.joel.cordappone.flows.bundlevisibility

import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CanSeeBundleInCoreSandbox @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.bundles.find { bundle -> bundle.symbolicName == "org.apache.felix.framework" }
            ?: throw IllegalStateException("CorDapp could not find core bundle.")
    }
}