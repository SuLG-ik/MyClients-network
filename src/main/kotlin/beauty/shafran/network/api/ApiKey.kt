package beauty.shafran.network.api

import beauty.shafran.network.config.SecureConfiguration
import get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*


private const val ApiKeyCode = "apiKey"

class ApiPrincipal : Principal

class ApiKeyAuthenticationProvider internal constructor(
    configuration: Configuration,
) : AuthenticationProvider(configuration) {
    internal val headerName: String = configuration.headerName

    internal val authenticationFunction = configuration.authenticationFunction

    internal val challengeFunction = configuration.challengeFunction

    internal val authScheme = configuration.authScheme

    /**
     * Api key auth configuration.
     */
    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name) {

        internal lateinit var authenticationFunction: ApiKeyAuthenticationFunction

        internal var challengeFunction: ApiKeyAuthChallengeFunction = {
            call.respond(HttpStatusCode.Unauthorized)
        }

        /**
         * Name of the scheme used when challenge fails, see [AuthenticationContext.challenge].
         */
        var authScheme: String = "apiKey"

        /**
         * Name of the header that will be used as a source for the api key.
         */
        var headerName: String = "X-Api-Key"

        /**
         * Sets a validation function that will check given API key retrieved from [headerName] instance and return [Principal],
         * or null if credential does not correspond to an authenticated principal.
         */
        fun validate(body: suspend ApplicationCall.(String) -> Principal?) {
            authenticationFunction = body
        }

        /**
         * A response to send back if authentication failed.
         */
        fun challenge(body: ApiKeyAuthChallengeFunction) {
            challengeFunction = body
        }
    }
}

fun Route.withApi(build: Route.() -> Unit) {
    val config = get<SecureConfiguration>()
    authenticate(config.keyName, build = build)
}

/**
 * Installs API Key authentication mechanism.
 */
fun Authentication.Configuration.apiKey(
    name: String? = null,
    configure: ApiKeyAuthenticationProvider.Configuration.() -> Unit,
) {
    val provider = ApiKeyAuthenticationProvider(ApiKeyAuthenticationProvider.Configuration(name).apply(configure))
    val headerName = provider.headerName
    val authenticate = provider.authenticationFunction
    val authScheme = provider.authScheme
    val challenge = provider.challengeFunction

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val apiKey = call.request.header(headerName)
        val principal = apiKey?.let { authenticate(call, it) }

        val cause = when {
            apiKey == null -> AuthenticationFailedCause.NoCredentials
            principal == null -> AuthenticationFailedCause.InvalidCredentials
            else -> null
        }
        if (cause != null) {
            context.challenge(authScheme, cause) {
                challenge.invoke(this, it)
                it.complete()
            }
        }
        if (principal != null) {
            context.principal(principal)
        }
    }

    register(provider)
}

/**
 * Alias for function signature that is invoked when verifying header.
 */
typealias ApiKeyAuthenticationFunction = suspend ApplicationCall.(String) -> Principal?

/**
 * Alias for function signature that is called when authentication fails.
 */
typealias ApiKeyAuthChallengeFunction = PipelineInterceptor<AuthenticationProcedureChallenge, ApplicationCall>