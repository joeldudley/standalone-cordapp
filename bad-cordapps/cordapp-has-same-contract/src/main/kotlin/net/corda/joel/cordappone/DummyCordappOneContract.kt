package net.corda.joel.cordappone

import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.transactions.LedgerTransaction

@Suppress("unused")
class DummyCordappOneContract : Contract {
    override fun verify(tx: LedgerTransaction) {}
}