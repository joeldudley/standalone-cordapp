package net.corda.joel.cordapptwo.flows.servicevisibility

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.joel.sharedlib.REGISTER_CORDAPP_SERVICE
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CanSeeServiceInOtherCpkCordappBundle : Flow<Unit> {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.getServiceReference(REGISTER_CORDAPP_SERVICE)
            ?: throw IllegalStateException("CorDapp could not find service in other CPK CorDapp bundle.")
    }
}