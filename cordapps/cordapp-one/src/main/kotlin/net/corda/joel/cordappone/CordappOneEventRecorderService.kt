package net.corda.joel.cordappone

import net.corda.v5.application.services.CordaService
import net.corda.v5.application.services.lifecycle.ServiceLifecycleEvent
import org.osgi.framework.FrameworkUtil

/** A service that listens for and stores service and bundle events. */
class CordappOneEventRecorderService : CordaService {
    val serviceEventSources = mutableSetOf<String?>()
    val bundleEventSources = mutableSetOf<String?>()

    override fun onEvent(event: ServiceLifecycleEvent) {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext

        bundleContext.addServiceListener { serviceEvent ->
            // We add the service event's source bundle to the set of service event sources.
            serviceEventSources.add(serviceEvent.serviceReference.bundle?.symbolicName)
        }

        bundleContext.addBundleListener { bundleEvent ->
            // We add the bundle's event source bundle to the set of bundle event sources.
            bundleEventSources.add(bundleEvent.origin?.symbolicName)
        }
    }
}