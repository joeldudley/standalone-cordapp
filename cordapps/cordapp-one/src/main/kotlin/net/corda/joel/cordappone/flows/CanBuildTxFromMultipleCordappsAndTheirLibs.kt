package net.corda.joel.cordappone.flows

import net.corda.joel.cordappone.DummyCordappOneContract
import net.corda.joel.cordappone.DummyCordappOneState
import net.corda.joel.cordapptwo.DummyCordappTwoCommand
import net.corda.systemflows.FinalityFlow
import net.corda.v5.application.flows.*
import net.corda.v5.application.flows.flowservices.FlowEngine
import net.corda.v5.application.flows.flowservices.FlowIdentity
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.services.NotaryLookupService
import net.corda.v5.ledger.transactions.TransactionBuilderFactory
import java.time.Duration

/**
 * Commits a transaction containing classes from multiple CorDapps and their libraries.
 *
 * Note that the library classes are embedded in the `DummyCordappOneState`, rather than added directly.
 */
@InitiatingFlow
@StartableByRPC
class CanBuildTxFromMultipleCordappsAndTheirLibs @JsonConstructor constructor(
    private val params: RpcStartFlowRequestParameters
) : Flow<Unit> {

    @CordaInject
    lateinit var notaryLookupService: NotaryLookupService

    @CordaInject
    lateinit var transactionBuilderFactory: TransactionBuilderFactory

    @CordaInject
    lateinit var flowIdentity: FlowIdentity

    @CordaInject
    lateinit var flowEngine: FlowEngine

    @Suspendable
    override fun call() {
        val notary = notaryLookupService.notaryIdentities.first()
        val txBuilder = transactionBuilderFactory.create().setNotary(notary)
            .addOutputState(DummyCordappOneState(), DummyCordappOneContract::class.java.name)
            .addCommand(DummyCordappTwoCommand(), flowIdentity.ourIdentity.owningKey)
        txBuilder.verify()
        val stx = txBuilder.sign()

        flowEngine.sleep(Duration.ZERO) // We force a checkpoint to ensure the transaction is (de)serializable.

        flowEngine.subFlow(FinalityFlow(stx, listOf()))
    }
}