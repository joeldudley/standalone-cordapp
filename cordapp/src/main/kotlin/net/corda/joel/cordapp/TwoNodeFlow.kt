package net.corda.joel.cordapp

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import net.joel.lib.ClassWithModifiableStatic
import org.osgi.framework.FrameworkUtil
import java.lang.IllegalStateException

@InitiatingFlow
@StartableByRPC
class CheckIsolatedLibsFlow1 : FlowLogic<Int>() {
    @Suspendable
    override fun call(): Int {
        return ClassWithModifiableStatic.modifiableStaticCounter++
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCanSeeBundlesInCoreSandbox : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.bundles.find { bundle -> bundle.symbolicName == "org.apache.felix.framework" }
            ?: throw IllegalStateException("CorDapp could not find core bundle.")
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCannotSeeBundlesInNonCoreSandbox : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val bundle = bundleContext.bundles.find { bundle -> bundle.symbolicName == "com.sun.xml.fastinfoset.FastInfoset" }
        if (bundle != null) throw IllegalStateException("CorDapp could find non-core bundle.")
    }
}

@InitiatingFlow
@StartableByRPC
class CheckServiceRegistryEmpty : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val service = bundleContext.getServiceReference("net.corda.sandbox.SandboxService")
        if (service != null) throw IllegalStateException("CorDapp could find non-core service.")
    }
}