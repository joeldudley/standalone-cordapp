package net.corda.joel.cordapp2

import net.corda.joel.cordapp.SetIsolatedLibStatic
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import net.joel.sharedlib.ClassWithModifiableStatic
import org.osgi.framework.FrameworkUtil

@InitiatingFlow
@StartableByRPC
class CheckIsolatedLibsFlow : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        if (ClassWithModifiableStatic.modifiableStaticCounter != 0)
            throw IllegalStateException("Static did not have the expected value.")
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCanSeeCordappBundleInOtherCpk : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        SetIsolatedLibStatic::class.java
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCannotSeeLibraryBundleInOtherCpk : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val bundle = bundleContext
            .bundles
            .find { bundle -> bundle.symbolicName == "cordapp-one-lib" }
        if (bundle != null) throw IllegalStateException("CorDapp could find library bundle in other CPK.")
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCannotSeeServiceInOtherCpkLibrary : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val service = bundleContext.getServiceReference("net.joel.cordapponelib.ClassThatRegistersService")
        if (service != null) throw IllegalStateException("CorDapp could find service in other CPK library.")
    }
}