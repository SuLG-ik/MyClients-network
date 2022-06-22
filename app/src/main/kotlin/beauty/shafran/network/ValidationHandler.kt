package beauty.shafran.network

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.web.bind.annotation.ControllerAdvice
import javax.validation.ValidationException

@ControllerAdvice
class ValidationHandler : DataFetcherExceptionResolverAdapter() {


    override fun resolveToMultipleErrors(ex: Throwable, env: DataFetchingEnvironment): List<GraphQLError>? {
        return resolveToSingleError(ex, env)?.let { listOf(it) }
    }

    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        if (ex is ValidationException)
            return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .apply {
                    if (ex.localizedMessage != null)
                        message(ex.localizedMessage)
                }
                .build()
        return null
    }

}