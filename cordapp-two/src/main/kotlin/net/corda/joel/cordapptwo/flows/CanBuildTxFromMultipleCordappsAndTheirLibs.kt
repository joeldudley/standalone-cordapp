package net.corda.joel.cordapptwo.flows

import net.corda.joel.cordappone.DummyCordappOneContract
import net.corda.joel.cordapptwo.DummyCordappTwoCommand
import net.corda.joel.cordapptwo.DummyCordappTwoState
import net.corda.systemflows.FinalityFlow
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.FlowEngine
import net.corda.v5.application.flows.flowservices.FlowIdentity
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.services.NotaryLookupService
import net.corda.v5.ledger.transactions.TransactionBuilderFactory

// TODO: I don't actually include any library classes here.
@InitiatingFlow
@StartableByRPC
class CanBuildTxFromMultipleCordappsAndTheirLibs : Flow<Unit> {
    @CordaInject
    lateinit var networkLookupService: NotaryLookupService

    @CordaInject
    lateinit var transactionBuilderFactory: TransactionBuilderFactory

    @CordaInject
    lateinit var flowIdentity: FlowIdentity

    @CordaInject
    lateinit var flowEngine: FlowEngine

    @Suspendable
    override fun call() {
        val notary = networkLookupService.notaryIdentities.first()
        val txBuilder = transactionBuilderFactory.create().setNotary(notary)
            .addOutputState(DummyCordappTwoState(), DummyCordappOneContract::class.java.name)
            .addCommand(DummyCordappTwoCommand(), flowIdentity.ourIdentity.owningKey)
        txBuilder.verify()
        val stx = txBuilder.sign()
        flowEngine.subFlow(FinalityFlow(stx, listOf()))
    }
}