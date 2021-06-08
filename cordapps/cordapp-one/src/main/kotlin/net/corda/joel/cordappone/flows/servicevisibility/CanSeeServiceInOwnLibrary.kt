package net.corda.joel.cordappone.flows.servicevisibility

import net.corda.v5.base.annotations.Suspendable
import net.corda.joel.sharedlib.LIBRARY_CLASS_THAT_REGISTERS_SERVICE
import net.corda.v5.application.flows.*
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CanSeeServiceInOwnLibrary @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.getServiceReference(LIBRARY_CLASS_THAT_REGISTERS_SERVICE)
            ?: throw IllegalStateException("CorDapp could not find service in own library.")
    }
}