package net.corda.joel.cordapptwo.flows.utility

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil
import java.util.*

/** Registers a service on the node. */
@InitiatingFlow
@StartableByRPC
class RegisterCordappService : Flow<Unit> {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(this::class.java.name, RegisterCordappService(), Hashtable<String, String>())
    }
}