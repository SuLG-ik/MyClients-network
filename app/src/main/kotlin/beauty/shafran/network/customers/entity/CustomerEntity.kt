package beauty.shafran.network.customers.entity

import beauty.shafran.network.cards.entity.CardTable
import beauty.shafran.network.gender.genderEnumeration
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object CustomerTable : LongIdWithMetaTable("customer")

@TableToCreation
@TableWithEntity
object CustomerDataTable : LongIdWithMetaTable("customer_data") {
    @PropertyOfEntity
    val customerId = reference("customer", CustomerTable)

    @PropertyOfEntity
    val name = varchar("name", 255)

    @PropertyOfEntity
    val gender = genderEnumeration("gender")

    @PropertyOfEntity
    val description = text("description")

}

@TableToCreation
@TableWithEntity
object CustomerToStorageTable : LongIdWithMetaTable("customer_reference_to_storage") {
    @PropertyOfEntity
    val customerId = reference("customer", CustomerTable)

    @PropertyOfEntity
    val storageId = reference("storage", CustomerStorageTable)
}

@TableToCreation
@TableWithEntity
object CustomerToCardTable : LongIdWithMetaTable("customer_reference_to_card") {
    @PropertyOfEntity
    val customerId = reference("customer", CustomerTable)

    @PropertyOfEntity
    val cardId = reference("card", CardTable)
}
