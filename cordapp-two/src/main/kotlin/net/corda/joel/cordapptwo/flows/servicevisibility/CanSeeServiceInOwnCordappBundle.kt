package net.corda.joel.cordapptwo.flows.servicevisibility

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CanSeeServiceInOwnCordappBundle : Flow<Unit> {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.getServiceReference("net.corda.joel.cordapptwo.flows.utility.RegisterCordappService")
            ?: throw IllegalStateException("CorDapp could not find service in own CorDapp bundle.")
    }
}