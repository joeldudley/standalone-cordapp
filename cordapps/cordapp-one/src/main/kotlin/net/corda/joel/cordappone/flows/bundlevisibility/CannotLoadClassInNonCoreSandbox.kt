package net.corda.joel.cordappone.flows.bundlevisibility

import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CannotLoadClassInNonCoreSandbox @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java)
        // We load a class from a non-core platform sandbox bundle package that this CorDapp imports.
        try {
            bundleContext.loadClass("net.corda.impl.application.identity.PartyImpl")
        } catch (e: ClassNotFoundException) {
            return
        }

        throw IllegalStateException("A `ClassNotFoundException` was not thrown when attempting to loaded a class in " +
                "a non-core sandbox.")
    }
}