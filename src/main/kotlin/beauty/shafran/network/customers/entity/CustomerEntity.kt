package beauty.shafran.network.customers.entity

import beauty.shafran.network.gender.Gender
import beauty.shafran.network.gender.genderEnumeration
import beauty.shafran.network.phone.entity.PhoneNumberEntity
import beauty.shafran.network.utils.LongIdWithMetaTable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

object CustomersTable : LongIdWithMetaTable("customers")

object CustomersDataTable : LongIdWithMetaTable("customers_data") {
    val customerId = reference("customerId", CustomersTable)
    val name = varchar("name", 255)
    val gender = genderEnumeration("gender")
    val description = varchar("description", 1000).nullable()
}


fun ResultRow.toCustomerDataEntity(): CustomerDataEntity {
    return CustomerDataEntity(
        name = get(CustomersDataTable.name),
        gender = get(CustomersDataTable.gender),
        description = get(CustomersDataTable.description),
        activationDate = get(CustomersDataTable.creationDate),
        phone = null,
    )
}

fun InsertStatement<Long>.toCustomerDataEntity(): CustomerDataEntity {
    return CustomerDataEntity(
        name = get(CustomersDataTable.name),
        gender = get(CustomersDataTable.gender),
        description = get(CustomersDataTable.description),
        activationDate = get(CustomersDataTable.creationDate),
        phone = null,
    )
}


@JvmInline
@Serializable
value class CustomerId(val id: Long)

class CustomerEntity(
    val id: CustomerId,
    val data: CustomerDataEntity,
)

data class CustomerDataEntity(
    val name: String,
    val gender: Gender,
    val activationDate: LocalDateTime,
    val phone: PhoneNumberEntity? = null,
    val description: String? = null,
)

