package net.corda.joel.cordappone

import net.corda.v5.application.node.services.CordaService
import net.corda.v5.base.internal.inputStream
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.transactions.LedgerTransaction
import org.osgi.framework.FrameworkUtil
import java.nio.file.Path
import java.util.*

class DummyCordappOneContract : Contract {
    override fun verify(tx: LedgerTransaction) {
    }
}

//class EventEmittingService: CordaService {
//    init {
//        // TODO: Change to service registration/unregistration.
//        val bc = FrameworkUtil.getBundle(this::class.java).bundleContext
//        val bundleUri = FrameworkUtil.getBundle(this::class.java).location
//        bc.installBundle("${Random().nextInt()}", Path.of(bundleUri).inputStream())
//    }
//}