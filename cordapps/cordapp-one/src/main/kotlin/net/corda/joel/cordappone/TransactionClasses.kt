package net.corda.joel.cordappone

import net.corda.joel.cordapponelib.LibraryClassThatRegistersService
import net.corda.joel.sharedlib.ClassWithModifiableStatic
import net.corda.v5.application.identity.Party
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData
import net.corda.v5.ledger.transactions.LedgerTransaction

class DummyCordappOneContract : Contract {
    override fun verify(tx: LedgerTransaction) {}
}

@BelongsToContract(DummyCordappOneContract::class)
class DummyCordappOneState : ContractState {
    override val participants = emptyList<Party>()

    // We embed library classes to ensure library classes can be (de)serialized as part of transactions.
    @Suppress("unused")
    val classFromOwnLibrary = LibraryClassThatRegistersService()

    @Suppress("unused")
    val classFromSharedLibrary = ClassWithModifiableStatic()
}

class DummyCordappOneCommand : TypeOnlyCommandData()