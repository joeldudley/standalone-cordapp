package net.joel.lib

/** A library with a modifiable static counter, used to test the isolation of library bundles between CPKs. */
class ClassWithModifiableStatic {
    companion object {
        var modifiableStaticCounter = 0
    }
}