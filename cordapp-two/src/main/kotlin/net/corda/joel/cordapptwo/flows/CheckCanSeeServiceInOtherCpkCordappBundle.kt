package net.corda.joel.cordapptwo.flows

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CheckCanSeeServiceInOtherCpkCordappBundle : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.getServiceReference("net.corda.joel.cordappone.CordappClassThatRegistersService")
            ?: throw IllegalStateException("CorDapp could not find service in other CPK CorDapp bundle.")
    }
}