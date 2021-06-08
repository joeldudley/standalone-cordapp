package net.corda.joel.cordapponelib

import org.osgi.framework.FrameworkUtil
import java.util.*

/** Allows us to perform service registration from a library class. */
class LibraryClassThatRegistersService {
    fun registerService() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(this::class.java.name, LibraryClassThatRegistersService(), Hashtable<String, String>())
    }
}