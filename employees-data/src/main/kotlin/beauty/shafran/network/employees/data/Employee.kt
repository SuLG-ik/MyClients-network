package beauty.shafran.network.employees.data

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class EmployeeId(
    val value: Long,
)

@Serializable
data class Employee(
    val id: EmployeeId,
    val data: EmployeeData,
)

@Serializable
data class EmployeeData(
    val name: String,
)