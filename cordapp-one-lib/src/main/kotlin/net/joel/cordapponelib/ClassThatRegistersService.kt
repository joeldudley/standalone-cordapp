package net.joel.cordapponelib

import org.osgi.framework.FrameworkUtil
import java.util.*

/** A library that registers a service. */
class ClassThatRegistersService {
    fun registerService() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(this::class.java.name, ClassThatRegistersService(), Hashtable<String, String>())
    }
}