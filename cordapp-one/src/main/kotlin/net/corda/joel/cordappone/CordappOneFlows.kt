package net.corda.joel.cordappone

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import net.joel.cordapponelib.ClassThatRegistersService
import net.joel.sharedlib.ClassWithModifiableStatic
import org.osgi.framework.FrameworkUtil
import kotlin.system.exitProcess

@InitiatingFlow
@StartableByRPC
class SetIsolatedLibStatic(private val value: Int) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        ClassWithModifiableStatic.modifiableStaticCounter = value
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
        val bundle = bundleContext.bundles.find {
                bundle -> bundle.symbolicName == "org.objenesis"
        }

        if (bundle != null) throw IllegalStateException("CorDapp could find non-core bundle.")
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCanLoadClassInCoreSandbox : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java)
        // We load a class from a core platform sandbox bundle package that this CorDapp imports.
        bundleContext.loadClass("net.corda.v5.application.identity.Party")
        // No `ClassNotFoundException` is thrown.
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCannotSeeServiceInNonCoreSandbox : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        val service = bundleContext.getServiceReference("net.corda.sandbox.SandboxService")
        if (service != null) throw IllegalStateException("CorDapp could find non-core service.")
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCanSeeLibraryBundleInOwnCpk : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext
            .bundles
            .find { bundle -> bundle.symbolicName == "cordapp-one-lib" }
            ?: throw IllegalStateException("CorDapp could not find library bundle in own CPK.")
    }
}

@InitiatingFlow
@StartableByRPC
class RegisterLibraryService : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        ClassThatRegistersService().registerService()
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCanSeeServiceInOwnCpkLibrary : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.getServiceReference("net.joel.cordapponelib.ClassThatRegistersService")
            ?: throw IllegalStateException("CorDapp could not find service in own library.")
    }
}

@InitiatingFlow
@StartableByRPC
class KillNode(private val setToFail: Boolean = false) : FlowLogic<Unit>() {
    companion object {
        // If we don't condition the exit on some variable, then when the node restarts it will rerun the flow and exit
        // again.
        private var shouldFail = false
    }

    @Suspendable
    override fun call() {
        if (setToFail) {
            shouldFail = true

        } else {
            if (shouldFail) {
                println("Process will hang.")
                exitProcess(0)
            }
        }
    }
}

@InitiatingFlow
@StartableByRPC
class CheckCanRestartFromCheckpoint(private val setToFail: Boolean = false) : FlowLogic<Unit>() {
    companion object {
        private var shouldFail = false
    }

    @Suspendable
    override fun call() {
        if (setToFail) {
            shouldFail = true

        } else {
            println("Flow started.")

            val msg = "messageSentToCreateCheckpoint"
            val counterparty = flowIdentity.ourIdentity
            flowMessaging.initiateFlow(counterparty).send(msg)

            println("Message sent.")

            if (shouldFail) {
                println("Process will hang.")
                exitProcess(0)
            }

            println("Flow continued.")
        }
    }
}