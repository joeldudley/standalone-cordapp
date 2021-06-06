package net.corda.joel.cordapptwo.flows

import net.corda.joel.cordappone.flows.utility.SetSharedLibStatic
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable

@InitiatingFlow
@StartableByRPC
class CheckCanSeeCordappBundleInOtherCpk : Flow<String> {
    @Suspendable
    override fun call(): String {
        return SetSharedLibStatic::class.java.name
    }
}