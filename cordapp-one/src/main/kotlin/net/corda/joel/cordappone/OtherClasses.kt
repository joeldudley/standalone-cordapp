package net.corda.joel.cordappone

import org.osgi.framework.FrameworkUtil
import java.util.*

/** A CorDapp class that registers a service. */
class CordappClassThatRegistersService {
    fun registerService() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(
            this::class.java.name,
            CordappClassThatRegistersService(),
            Hashtable<String, String>()
        )
    }
}