package beauty.shafran.network.customers.data

import beauty.shafran.network.Gender
import beauty.shafran.network.phone.data.PhoneNumber
import beauty.shafran.network.validation.ObjectIdParameter
import kotlinx.serialization.Serializable
import javax.validation.Valid
import javax.validation.constraints.Size

@Serializable
data class EditCustomerRequest(
    @field:ObjectIdParameter
    val customerId: String,
    @field:Valid
    val data: EditableCustomerData,
)

@Serializable
data class EditableCustomerData(
    @field:Size(min = 2)
    val name: String,
    val phone: PhoneNumber?,
    val remark: String,
    val gender: Gender,
)

fun EditableCustomerData.trim(): EditableCustomerData {
    return copy(name = name.trim(), remark = remark.trim())
}


fun EditCustomerRequest.trim(): EditCustomerRequest {
    return copy(data = data.trim())
}

@Serializable
data class EditCustomerResponse(
    val customer: Customer.ActivatedCustomer,
)
