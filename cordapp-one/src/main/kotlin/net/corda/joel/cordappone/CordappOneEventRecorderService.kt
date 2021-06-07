package net.corda.joel.cordappone

import net.corda.v5.application.services.CordaService
import net.corda.v5.application.services.lifecycle.ServiceLifecycleEvent
import org.osgi.framework.FrameworkUtil

class CordappOneEventRecorderService : CordaService {
    val serviceEventSources = mutableSetOf<String>()

    override fun onEvent(event: ServiceLifecycleEvent) {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext

        bundleContext.addServiceListener { serviceEvent ->
            // We add the service event's source bundle to the set of service event sources.
            serviceEventSources.add(serviceEvent.serviceReference.bundle.symbolicName)
        }
    }
}