package net.corda.joel.cordappone.flows.bundlevisibility

import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CanLoadClassInCoreSandbox @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java)
        // We load a class from a core platform sandbox bundle package that this CorDapp imports.
        bundleContext.loadClass("net.corda.v5.application.identity.Party")
        // No `ClassNotFoundException` is thrown.
    }
}