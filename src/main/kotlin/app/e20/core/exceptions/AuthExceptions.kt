package app.e20.core.exceptions

/**
 * The entity is not authenticated
 */
class AuthenticationException(override val message: String? = null) : RuntimeException(message)

/**
 * The entity is not authorized
 */
class AuthorizationException(override val message: String? = null) : RuntimeException(message)
