package net.corda.joel.cordapptwo.flows.servicevisibility

import net.corda.joel.sharedlib.REGISTER_CORDAPP_AND_LIBRARY_SERVICES
import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CanSeeServiceInOtherCpkCordappBundle @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.getServiceReference(REGISTER_CORDAPP_AND_LIBRARY_SERVICES)
            ?: throw IllegalStateException("CorDapp could not find service in other CPK CorDapp bundle.")
    }
}