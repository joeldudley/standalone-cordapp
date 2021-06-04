package net.corda.joel.cordappone.flows.utility

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import org.osgi.framework.FrameworkUtil
import java.util.*

@InitiatingFlow
@StartableByRPC
class RegisterCordappService : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(this::class.java.name, RegisterCordappService(), Hashtable<String, String>())
    }
}