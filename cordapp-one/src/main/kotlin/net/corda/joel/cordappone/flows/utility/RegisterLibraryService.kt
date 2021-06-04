package net.corda.joel.cordappone.flows.utility

import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic
import net.joel.sharedlib.LibraryClassThatRegistersService

@InitiatingFlow
@StartableByRPC
class RegisterLibraryService : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        LibraryClassThatRegistersService().registerService()
    }
}