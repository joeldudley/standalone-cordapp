package net.corda.joel.cordappone

import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.transactions.LedgerTransaction

class DummyCordappOneContract : Contract {
    override fun verify(tx: LedgerTransaction) {}
}