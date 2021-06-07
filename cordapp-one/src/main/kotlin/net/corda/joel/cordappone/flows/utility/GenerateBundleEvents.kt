package net.corda.joel.cordappone.flows.utility

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.joel.sharedlib.CORDAPP_TO_BE_STOPPED_AND_STARTED
import net.joel.sharedlib.SHARED_LIB
import org.osgi.framework.FrameworkUtil

/** Generates bundle events by restarting one of CorDapp One's library bundles and CorDapp Two's CorDapp bundle. */
@InitiatingFlow
@StartableByRPC
class GenerateBundleEvents : Flow<Unit> {
    @Suspendable
    override fun call() {
        val allBundles = FrameworkUtil.getBundle(this::class.java).bundleContext.bundles

        val sharedLibraryBundle = allBundles.find { bundle -> bundle.symbolicName == SHARED_LIB }!!
        sharedLibraryBundle.stop()
        sharedLibraryBundle.start()

        val cordappBundle = allBundles.find { bundle -> bundle.symbolicName == CORDAPP_TO_BE_STOPPED_AND_STARTED }!!
        cordappBundle.stop()
        cordappBundle.start()
    }
}