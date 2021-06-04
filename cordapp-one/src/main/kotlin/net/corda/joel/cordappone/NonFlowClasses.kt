package net.corda.joel.cordappone

import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.transactions.LedgerTransaction
import org.osgi.framework.FrameworkUtil
import java.util.*

class DummyCordappOneContract : Contract {
    override fun verify(tx: LedgerTransaction) {
    }
}

/** A CorDapp class that registers a service. */
class CordappClassThatRegistersService {
    fun registerService() {
        val bundleContext = FrameworkUtil.getBundle(this::class.java).bundleContext
        bundleContext.registerService(this::class.java.name, CordappClassThatRegistersService(), Hashtable<String, String>())
    }
}