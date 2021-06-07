package net.corda.joel.cordappone.flows

import net.corda.joel.cordappone.DummyCordappOneCommand
import net.corda.joel.cordappone.DummyCordappOneContract
import net.corda.joel.cordappone.DummyCordappOneState
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
import java.time.Duration

/**
 * Checks that a transaction built from a single CorDapp can be committed successfully.
 *
 * We get additional smoke-testing for free by making this flow as complicated as possible. This explains any
 * unnecessary variables, operations, etc.
 */
@InitiatingFlow
@StartableByRPC
class CanCommitTx : Flow<Unit> {
    @CordaInject
    lateinit var networkLookupService: NotaryLookupService

    @CordaInject
    lateinit var transactionBuilderFactory: TransactionBuilderFactory

    @CordaInject
    lateinit var flowEngine: FlowEngine

    @CordaInject
    lateinit var flowIdentity: FlowIdentity

    @Suspendable
    override fun call() {
        val notary = networkLookupService.notaryIdentities.first()
        val txBuilder = transactionBuilderFactory.create().setNotary(notary)
            .addOutputState(DummyCordappOneState(), DummyCordappOneContract::class.java.name)
            .addCommand(DummyCordappOneCommand(), flowIdentity.ourIdentity.owningKey)
        txBuilder.verify()
        val stx = txBuilder.sign()

        flowEngine.sleep(Duration.ZERO) // We force a checkpoint to ensure the transaction is (de)serializable.

        flowEngine.subFlow(FinalityFlow(stx, listOf()))
    }
}