package net.corda.joel.cordappone.flows.utility

import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.joel.sharedlib.ClassWithModifiableStatic

@InitiatingFlow
@StartableByRPC
class SetSharedLibStatic(private val value: Int) : Flow<Unit> {
    @Suspendable
    override fun call() {
        ClassWithModifiableStatic.modifiableStaticCounter = value
    }
}