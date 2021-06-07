package net.joel.sharedlib

import org.osgi.framework.FrameworkUtil
import java.util.*

/** A library class that registers a service. */
class LibraryClassThatRegistersService {
    fun registerService() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(this::class.java.name, LibraryClassThatRegistersService(), Hashtable<String, String>())
    }
}