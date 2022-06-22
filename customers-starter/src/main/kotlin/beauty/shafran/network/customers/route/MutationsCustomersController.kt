package beauty.shafran.network.customers.route

import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.customers.entities.*
import beauty.shafran.network.customers.repository.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import kotlin.random.asKotlinRandom

class CustomersMutation

class AddCustomerWithCardInput(
    val companyId: Long,
    val name: String,
    val description: String?,
)

@Controller
class MutationsCustomersController(
    private val customerRepository: CustomerRepository,
    private val customerDataRepository: CustomerDataRepository,
    private val customerCardRepository: CustomerCardRepository,
    private val customerToCompanyRepository: CustomerToCompanyRepository,
    private val companyRepository: CompanyRepository,
    private val cardRepository: CardRepository,
) {

    @MutationMapping
    fun customers() = CustomersMutation()


    @SchemaMapping(typeName = "CustomersMutation")
    @Transactional
    fun addCustomerAndCard(@Argument input: AddCustomerWithCardInput): Card {
        val token = generateToken(24)
        val companyReference = companyRepository.getReferenceById(input.companyId)
        val card = cardRepository.save(CardEntity(token = token, company = companyReference))
        val customer = customerRepository.save(CustomerEntity(name = input.name))
        if (input.description != null)
            customerDataRepository.save(CustomerDataEntity(customer = customer, description = input.description))
        customerCardRepository.save(CustomerCardTokenEntity(card = card, customer = customer))
        customerToCompanyRepository.save(CustomerToCompanyEntity(customer = customer, company = companyReference))
        return Card(
            id = card.id,
            token = card.token,
            customerId = customer.id
        )
    }


    companion object {

        private const val template = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

        private val random = SecureRandom().asKotlinRandom()

        fun generateToken(length: Int): String {
            return buildString { repeat(length) { append(template.random(random)) } }
        }

    }

}