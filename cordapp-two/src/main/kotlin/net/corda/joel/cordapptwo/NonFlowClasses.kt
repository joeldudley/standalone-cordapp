package net.corda.joel.cordapptwo

import net.corda.joel.cordappone.DummyCordappOneContract
import net.corda.v5.application.identity.Party
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData

@BelongsToContract(DummyCordappOneContract::class)
class DummyCordappTwoState(override val participants: List<Party> = emptyList()) : ContractState

class DummyCordappTwoCommand : TypeOnlyCommandData()