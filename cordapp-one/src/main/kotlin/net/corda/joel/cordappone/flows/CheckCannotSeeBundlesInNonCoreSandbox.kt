package net.corda.joel.cordappone.flows

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CheckCannotSeeBundlesInNonCoreSandbox : Flow<Unit> {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val bundle = bundleContext.bundles.find {
                bundle -> bundle.symbolicName == "org.objenesis"
        }

        if (bundle != null) throw IllegalStateException("CorDapp could find non-core bundle.")
    }
}