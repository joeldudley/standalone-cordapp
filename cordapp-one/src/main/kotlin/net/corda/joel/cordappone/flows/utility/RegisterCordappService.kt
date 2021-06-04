package net.corda.joel.cordappone.flows.utility

import net.corda.joel.cordappone.CordappClassThatRegistersService
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.legacyapi.flows.FlowLogic

@InitiatingFlow
@StartableByRPC
class RegisterCordappService : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        CordappClassThatRegistersService().registerService()
    }
}