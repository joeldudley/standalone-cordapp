package net.corda.joel.cordappone

import net.corda.joel.cordapponelib.LibraryClass
import net.corda.v5.application.identity.Party
import net.corda.v5.application.utilities.JsonRepresentable
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData
import net.corda.v5.ledger.transactions.LedgerTransaction

// Since we serialize these classes as part of flows, we get additional smoke-testing for free by making them as
// complicated as possible. This explains any unnecessary fields, interfaces, etc.

class DummyCordappOneContract : Contract {
    override fun verify(tx: LedgerTransaction) {}
}

@BelongsToContract(DummyCordappOneContract::class)
class DummyCordappOneState(
    override val participants: List<Party> = emptyList()
) : ContractState, JsonRepresentable {

    // TODO - Including a library class in a transaction doesn't work currently.
    // @Suppress("unused")
    // val libraryClass = LibraryClass() // We embed a library class for smoke-testing purposes.

    override fun toJsonString(): String {
        return """{ "participants": "${participants.joinToString(",")}" }"""
    }
}

class DummyCordappOneCommand : TypeOnlyCommandData()