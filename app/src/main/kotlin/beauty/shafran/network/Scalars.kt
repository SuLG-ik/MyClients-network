package beauty.shafran.network

import graphql.language.ScalarTypeDefinition
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer


@Configuration
public class Scalars : RuntimeWiringConfigurer {

    override fun configure(builder: RuntimeWiring.Builder) {
        val localDateTimeScalar = GraphQLScalarType.newScalar()
            .name("LocalDateTime")
            .coercing(GraphQlDateCoercing())
            .build()
        builder
            .scalar(localDateTimeScalar)
    }

}