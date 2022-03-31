package beauty.shafran.network.employees.entity

import beauty.shafran.network.Gender
import beauty.shafran.network.assets.entity.AssetEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class EmployeeEntity(
    val data: EmployeeDataEntity,
    val image: AssetEntity? = null,
    val layoff: EmployeeLayoffEntity? = null,
    @SerialName("_id")
    @Contextual
    val id: Id<EmployeeEntity> = newId(),
)

val EmployeeEntity.Companion.collectionName get() = "employees"

@Serializable
data class EmployeeDataEntity(
    val name: String,
    val description: String,
    val gender: Gender,
    val isPublic: Boolean = false,
)

@Serializable
data class EmployeeLayoffEntity(
    val reason: String,
    @Contextual
    @SerialName("_id")
    val id: Id<EmployeeLayoffEntity> = newId(),
)
