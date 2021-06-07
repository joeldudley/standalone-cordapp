package net.corda.joel.cordappone.flows.utility

import net.corda.joel.cordapptwo.flows.utility.SetSharedLibStatic
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.joel.sharedlib.ClassWithModifiableStatic
import org.osgi.framework.FrameworkUtil

/** Generates bundle events by restarting one of CorDapp One's library bundles and CorDapp Two's CorDapp bundle. */
@InitiatingFlow
@StartableByRPC
class GenerateBundleEvents : Flow<Unit> {
    @Suspendable
    override fun call() {
        val sharedLibraryBundle = FrameworkUtil.getBundle(ClassWithModifiableStatic::class.java)

        sharedLibraryBundle.stop()
        sharedLibraryBundle.start()

        val cordappTwoBundle = FrameworkUtil.getBundle(SetSharedLibStatic::class.java)
        cordappTwoBundle.stop()
        cordappTwoBundle.start()
    }
}