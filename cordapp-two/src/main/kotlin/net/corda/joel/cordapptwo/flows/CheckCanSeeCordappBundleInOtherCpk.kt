package net.corda.joel.cordapptwo.flows

import net.corda.joel.cordappone.flows.utility.SetSharedLibStatic
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic

@InitiatingFlow
@StartableByRPC
class CheckCanSeeCordappBundleInOtherCpk : FlowLogic<String>() {
    @Suspendable
    override fun call(): String {
        return SetSharedLibStatic::class.java.name
    }
}