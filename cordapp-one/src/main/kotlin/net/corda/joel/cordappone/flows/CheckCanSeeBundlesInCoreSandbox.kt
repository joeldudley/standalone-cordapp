package net.corda.joel.cordappone.flows

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CheckCanSeeBundlesInCoreSandbox : Flow<Unit> {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.bundles.find { bundle -> bundle.symbolicName == "org.apache.felix.framework" }
            ?: throw IllegalStateException("CorDapp could not find core bundle.")
    }
}