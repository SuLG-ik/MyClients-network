package beauty.shafran.network.customers.route

import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.companies.route.Company
import beauty.shafran.network.customers.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

class CustomersQuery

class Card(
    val id: Long,
    val token: String,
    val customerId: Long?,
)

class Customer(
    val id: Long,
    val name: String,
)

class CustomerData(
    val customer: Customer,
)

@Controller
class CustomersController(
    private val customerRepository: CustomerRepository,
    private val customerDataRepository: CustomerDataRepository,
    private val customerCardRepository: CustomerCardRepository,
    private val customerToCompanyRepository: CustomerToCompanyRepository,
    private val companyRepository: CompanyRepository,
    private val cardRepository: CardRepository,
) {

    @QueryMapping
    fun customers() = CustomersQuery()

    @Transactional
    @SchemaMapping(typeName = "CustomersQuery")
    fun card(@Argument token: String): Card {
        val card = cardRepository.findByToken(token) ?: TODO()
        val customerCard = customerCardRepository.findByCard(card)
            ?: return Card(
                id = card.id,
                token = card.token,
                customerId = null
            )
        return Card(
            id = card.id,
            token = card.token,
            customerId = customerCard.customer.id
        )
    }

    @Transactional
    @SchemaMapping(typeName = "CustomersQuery")
    fun customer(@Argument id: Long): Customer {
        val customer = customerRepository.findByIdOrNull(id) ?: TODO()
        return Customer(
            id = customer.id,
            name = customer.name
        )
    }

    @Transactional
    @SchemaMapping
    fun customers(source: Company): List<Customer> {
        return customerToCompanyRepository.findAllByCompany(companyRepository.getReferenceById(source.id))
            .map {
                Customer(
                    id = it.customer.id,
                    name = it.customer.name
                )
            }
    }

    @Transactional
    @SchemaMapping
    fun customer(source: Card): Customer? {
        if (source.customerId == null)
            return null
        val customer = customerRepository.findByIdOrNull(source.customerId) ?: TODO()
        return Customer(
            id = customer.id,
            name = customer.name
        )
    }


    @Transactional
    @SchemaMapping
    fun data(source: Customer): CustomerData {
        return CustomerData(source)
    }

    @Transactional
    @SchemaMapping
    fun card(source: Customer): Card? {
        val card = customerCardRepository.findByCustomer(customerRepository.getReferenceById(source.id)) ?: return null
        return Card(
            id = card.card.id,
            token = card.card.token,
            customerId = source.id
        )
    }


    @Transactional
    @SchemaMapping
    fun name(source: CustomerData): String {
        return source.customer.name
    }


    @Transactional
    @SchemaMapping
    fun description(source: CustomerData): String? {
        return customerDataRepository.findByCustomer(customerRepository.getReferenceById(source.customer.id))?.description
    }

}