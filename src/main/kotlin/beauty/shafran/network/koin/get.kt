import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier


fun Application.getKoin(): Koin = GlobalContext.get()

/**
 * inject lazily given dependency
 * @param qualifier - bean name / optional
 * @param scope
 * @param parameters
 */
inline fun <reified T : Any> Application.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    lazy { get<T>(qualifier, parameters) }

/**
 * Retrieve given dependency for KoinComponent
 * @param qualifier - bean name / optional
 * @param scope
 * @param parameters
 */
inline fun <reified T : Any> Application.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    getKoin().get<T>(qualifier, parameters)

/**
 * Retrieve given property for KoinComponent
 * @param key - key property
 */
fun <T : Any> Application.getProperty(key: String) =
    getKoin().getProperty<T>(key)

/**
 * Retrieve given property for KoinComponent
 * give a default value if property is missing
 *
 * @param key - key property
 * @param defaultValue - default value if property is missing
 *
 */
fun Application.getProperty(key: String, defaultValue: String) =
    getKoin().getProperty(key) ?: defaultValue

inline fun <reified T : Any> Routing.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    lazy { get<T>(qualifier, parameters) }

/**
 * Retrieve given dependency for KoinComponent
 * @param qualifier - bean name / optional
 * @param parameters
 */
inline fun <reified T : Any> Routing.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    getKoin().get<T>(qualifier, parameters)

/**
 * Retrieve given property for KoinComponent
 * @param key - key property
 */
fun <T : Any> Routing.getProperty(key: String) =
    getKoin().getProperty<T>(key)

/**
 * Retrieve given property for KoinComponent
 * give a default value if property is missing
 *
 * @param key - key property
 * @param defaultValue - default value if property is missing
 *
 */
inline fun <reified T> Routing.getProperty(key: String, defaultValue: T) =
    getKoin().getProperty(key) ?: defaultValue

/**
 * Help work on ModuleDefinition
 */
fun Routing.getKoin() = GlobalContext.get()

inline fun <reified T : Any> Route.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    lazy { get<T>(qualifier, parameters) }

/**
 * Retrieve given dependency for KoinComponent
 * @param qualifier - bean name / optional
 * @param parameters
 */
inline fun <reified T : Any> Route.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    getKoin().get<T>(qualifier, parameters)

/**
 * Retrieve given property for KoinComponent
 * @param key - key property
 */
fun <T : Any> Route.getProperty(key: String) =
    getKoin().getProperty<T>(key)

/**
 * Retrieve given property for KoinComponent
 * give a default value if property is missing
 *
 * @param key - key property
 * @param defaultValue - default value if property is missing
 *
 */
fun Route.getProperty(key: String, defaultValue: String) =
    getKoin().getProperty(key) ?: defaultValue

/**
 * Help work on ModuleDefinition
 */
fun Route.getKoin() = GlobalContext.get()