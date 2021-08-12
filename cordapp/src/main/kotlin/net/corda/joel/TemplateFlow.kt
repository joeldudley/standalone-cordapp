package net.corda.joel

import net.corda.v5.application.flows.*
import net.corda.v5.application.flows.flowservices.FlowIdentity
import net.corda.v5.application.identity.AbstractParty
import net.corda.v5.application.injection.CordaInject
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData
import net.corda.v5.ledger.services.NotaryLookupService
import net.corda.v5.ledger.transactions.LedgerTransaction
import net.corda.v5.ledger.transactions.TransactionBuilderFactory

@InitiatingFlow
@StartableByRPC
class TemplateFlow @JsonConstructor constructor(private val params: RpcStartFlowRequestParameters) : Flow<Unit> {
    @CordaInject
    lateinit var flowIdentity: FlowIdentity
    @CordaInject
    lateinit var transactionBuilderFactory: TransactionBuilderFactory
    @CordaInject
    lateinit var notaryLookup: NotaryLookupService

    @Suspendable
    override fun call() {
        println("Doing stuff.")
    }
}

class DummyContract : Contract {
    override fun verify(tx: LedgerTransaction) = Unit
}

@BelongsToContract(DummyContract::class)
class DummyState(override val participants: List<AbstractParty> = emptyList()) : ContractState

object DummyCommand : TypeOnlyCommandData()