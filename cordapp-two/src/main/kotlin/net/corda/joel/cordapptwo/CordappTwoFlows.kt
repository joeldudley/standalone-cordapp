package net.corda.joel.cordapptwo

import net.corda.joel.cordappone.DummyCordappOneContract
import net.corda.joel.cordappone.SetIsolatedLibStatic
import net.corda.systemflows.FinalityFlow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.services.NotaryLookupService
//import net.corda.v5.ledger.services.NotaryAwareNetworkMapCache
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

// TODO: I don't actually include any library classes here.
@InitiatingFlow
@StartableByRPC
class CheckCanBuildTxFromMultipleFlowsAndTheirLibs : FlowLogic<Unit>() {
    @CordaInject
    lateinit var networkLookupService: NotaryLookupService

    @Suspendable
    override fun call() {
        val notary = networkLookupService.notaryIdentities.first()
        val txBuilder = transactionBuilderFactory.create().setNotary(notary)
            .addOutputState(DummyCordappTwoState(), DummyCordappOneContract::class.java.name)
            .addCommand(DummyCordappTwoCommand(), ourIdentity.owningKey)
        txBuilder.verify()
        val stx = txBuilder.sign()
        flowEngine.subFlow(FinalityFlow(stx, listOf()))
    }
}