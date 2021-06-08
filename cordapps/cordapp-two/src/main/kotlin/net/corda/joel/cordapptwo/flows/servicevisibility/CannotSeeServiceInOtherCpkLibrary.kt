package net.corda.joel.cordapptwo.flows.servicevisibility

import net.corda.v5.base.annotations.Suspendable
import net.corda.joel.sharedlib.LIBRARY_CLASS_THAT_REGISTERS_SERVICE
import net.corda.v5.application.flows.*
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CannotSeeServiceInOtherCpkLibrary @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val service = bundleContext.getServiceReference(LIBRARY_CLASS_THAT_REGISTERS_SERVICE)
        if (service != null) throw IllegalStateException("CorDapp could find service in other CPK library.")
    }
}