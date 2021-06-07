package net.corda.joel.cordappone.flows.servicevisibility

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CannotSeeServiceInOtherCpkLibrary : Flow<Unit> {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val service = bundleContext.getServiceReference("net.joel.sharedlib.LibraryClassThatRegistersService")
        if (service != null) throw IllegalStateException("CorDapp could find service in other CPK library.")
    }
}