package net.corda.joel.cordappone.flows

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CheckCanLoadClassInCoreSandbox : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java)
        // We load a class from a core platform sandbox bundle package that this CorDapp imports.
        bundleContext.loadClass("net.corda.v5.application.identity.Party")
        // No `ClassNotFoundException` is thrown.
    }
}