package net.corda.joel.cordappone.flows.utility

import net.corda.joel.cordapponelib.LibraryClassThatRegistersService
import net.corda.v5.application.flows.Flow
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.base.annotations.Suspendable

/**
 * Calls into a library class to register a service on the node.
 */
@InitiatingFlow
@StartableByRPC
class RegisterLibraryService : Flow<Unit> {
    @Suspendable
    override fun call() {
        LibraryClassThatRegistersService().registerService()
    }
}