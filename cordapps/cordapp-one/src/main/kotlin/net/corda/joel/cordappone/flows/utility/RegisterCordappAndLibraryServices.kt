package net.corda.joel.cordappone.flows.utility

import net.corda.joel.cordapponelib.LibraryClassThatRegistersService
import net.corda.v5.application.flows.*
import net.corda.v5.base.annotations.Suspendable
import org.osgi.framework.FrameworkUtil
import java.util.*

/** Registers a service on the node. */
@InitiatingFlow
@StartableByRPC
class RegisterCordappAndLibraryServices @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(
            this::class.java.name,
            RegisterCordappAndLibraryServices(RpcStartFlowRequestParameters("")),
            Hashtable<String, String>())

        LibraryClassThatRegistersService().registerService()
    }
}