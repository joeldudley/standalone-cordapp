package net.corda.joel.cordapp2

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import net.joel.lib.ClassWithModifiableStatic

/** Shares a class name but not a fully-qualified class name with the `TwoNodeFlow` in `:joel:cordapp`. */
@InitiatingFlow
@StartableByRPC
class CheckIsolatedLibsFlow2 : FlowLogic<Int>() {
    @Suspendable
    override fun call(): Int {
        return ClassWithModifiableStatic.modifiableStaticCounter++
    }
}