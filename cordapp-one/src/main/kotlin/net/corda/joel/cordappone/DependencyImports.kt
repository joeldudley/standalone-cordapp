package net.corda.joel.cordappone

/** This function accesses packages from various popular dependencies to force the main CPK bundle to import them. */
@Suppress("unused")
fun forceDependencyImports() {
    println(com.google.common.util.concurrent.internal.InternalFutureFailureAccess::class.java)

    println(com.fasterxml.jackson.annotation.JacksonAnnotation::class.java)
    println(com.fasterxml.jackson.core.Version::class.java)
    println(com.fasterxml.jackson.databind.AbstractTypeResolver::class.java)
    println(com.google.common.annotations.Beta::class.java)
    println(com.google.gson.JsonDeserializer::class.java)
    println(com.mysql.cj.CacheAdapterFactory::class.java)
    println(javax.annotation.CheckForNull::class.java)
    println(okhttp3.Authenticator::class.java)
    println(org.apache.commons.codec.BinaryDecoder::class.java)
    println(org.apache.commons.io.ByteOrderMark::class.java)
    println(org.apache.commons.lang.ArrayUtils::class.java)
    println(org.apache.commons.lang3.AnnotationUtils::class.java)
    println(org.apache.commons.logging.Log::class.java)
    println(org.apache.log4j.Appender::class.java)
    println(org.apache.logging.log4j.CloseableThreadContext::class.java)
    println(org.joda.time.Chronology::class.java)
    println(scala.Equals::class.java)
}