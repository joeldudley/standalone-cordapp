package net.corda.joel.cordappone

import net.corda.v5.application.identity.Party
import net.corda.v5.application.services.CordaService
import net.corda.v5.application.services.lifecycle.ServiceLifecycleEvent
import net.corda.v5.application.utilities.JsonRepresentable
import net.corda.v5.base.util.contextLogger
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData
import net.corda.v5.ledger.transactions.LedgerTransaction
import org.osgi.framework.FrameworkUtil

class DummyCordappOneContract : Contract {
    override fun verify(tx: LedgerTransaction) {}
}

@BelongsToContract(DummyCordappOneContract::class)
class DummyCordappOneState(
    override val participants: List<Party> = emptyList()
) : ContractState, JsonRepresentable {

    // TODO - Including a library class in a transaction doesn't work currently.
    // We embed a library class to check whether library classes can be (de)serialized.
    // @Suppress("unused")
    // val libraryClass = LibraryClass()

    override fun toJsonString() = ""
}

class DummyCordappOneCommand : TypeOnlyCommandData()

class EventRecorderService : CordaService {
    companion object {
        private val logger = contextLogger()
    }

    init {
        logger.info("in init")
        println("in init")
    }

    override fun onEvent(event: ServiceLifecycleEvent) {
        logger.info("service started")
        println("Service started")
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.addBundleListener { bundleEvent ->
            println(bundleEvent)
        }
        bundleContext.addServiceListener { serviceEvent ->
            println(serviceEvent)
        }
    }
}