package beauty.shafran.network

import graphql.schema.Coercing
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

internal class GraphQlDateCoercing : Coercing<LocalDateTime, String> {

    override fun serialize(dataFetcherResult: Any): String {
        if (dataFetcherResult !is LocalDateTime)
            throw CoercingSerializeException("Input is not LocalDateTime")
        return dataFetcherResult.toString()
    }

    override fun parseValue(input: Any): LocalDateTime {
        if (input !is String)
            throw CoercingParseValueException("Serialized LocalDateTime is String")
        return try {
            LocalDateTime.parse(input)
        } catch (e: DateTimeParseException) {
            throw CoercingParseValueException("Serialized LocalDateTime is illegal")
        }
    }

    override fun parseLiteral(input: Any): LocalDateTime {
        if (input !is String)
            throw CoercingParseValueException("Serialized LocalDateTime is String")
        return try {
            LocalDateTime.parse(input)
        } catch (e: DateTimeParseException) {
            throw CoercingParseValueException("Serialized LocalDateTime is illegal")
        }
    }


}