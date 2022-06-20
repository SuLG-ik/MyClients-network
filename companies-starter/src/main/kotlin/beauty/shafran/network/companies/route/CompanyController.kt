package beauty.shafran.network.companies.route

import beauty.shafran.network.accounts.route.Account
import beauty.shafran.network.companies.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

class CompanyQuery(
    var company: Company? = null,
)

class Company(
    var id: Long,
    var title: String,
    var codename: String,
)

class CompanyData(
    var title: String,
)

class CompanyMember(
    var id: Long,
    var account: Account,
)

class CompanyOwner(
    var id: Long,
    var member: CompanyMember? = null,
)

class CompanyPlacementMember(
    var id: Long,
    var placementId: Long,
    var account: Account? = null,
)

class CompanyPlacementData(
    var title: String,
)

class CompanyPlacement(
    val id: Long,
    val companyId: Long,
    val codename: String,
    val title: String,
)

@Controller
class CompanyController(
    private val companyRepository: CompanyRepository,
    private val companyOwnerRepository: CompanyOwnerRepository,
    private val companyMemberRepository: CompanyMemberRepository,
    private val companyPlacementRepository: CompanyPlacementRepository,
    private val companyPlacementMemberRepository: CompanyPlacementMemberRepository,
) {

    @QueryMapping
    fun companies(): CompanyQuery {
        return CompanyQuery()
    }

    @Transactional
    @SchemaMapping(typeName = "CompanyQuery")
    fun company(
        @Argument id: String?,
        @Argument codename: String?,
    ): Company {
        val company = when {
            id != null -> {
                companyRepository.findByIdOrNull(id.toLong())
            }

            codename != null -> {
                companyRepository.findByCodename(codename)
            }

            else -> TODO()
        }!!
        return Company(
            id = company.id,
            codename = company.codename,
            title = company.title,
        )
    }

    @Transactional
    @SchemaMapping
    fun data(source: Company): CompanyData {
        return CompanyData(
            title = source.title
        )
    }

    @Transactional
    @SchemaMapping
    fun owner(source: Company): CompanyOwner {
        val owner = companyOwnerRepository.findByCompany(companyRepository.getReferenceById(source.id))!!
        val account = owner.member.account
        return CompanyOwner(
            id = owner.id,
            member = CompanyMember(
                id = owner.member.id,
                account = Account(
                    id = account.id,
                    username = account.username
                )
            )
        )
    }

    @Transactional
    @SchemaMapping
    fun members(source: Company): List<CompanyMember> {
        val members = companyMemberRepository.findAllByCompany(companyRepository.getReferenceById(source.id))
        return members.map {
            CompanyMember(
                id = it.id,
                account = Account(
                    id = it.account.id,
                    username = it.account.username
                )
            )
        }
    }

    @Transactional
    @SchemaMapping
    fun placements(source: Company): List<CompanyPlacement> {
        val placements = companyPlacementRepository.findAllByCompany(companyRepository.getReferenceById(source.id))
        return placements.map {
            CompanyPlacement(
                id = it.id,
                companyId = source.id,
                codename = it.codename,
                title = it.title
            )
        }
    }


    @Transactional
    @SchemaMapping
    fun data(source: CompanyPlacement): CompanyPlacementData {
        return CompanyPlacementData(title = source.title)
    }


    @Transactional
    @SchemaMapping
    fun members(source: CompanyPlacement): List<CompanyPlacementMember> {
        return companyPlacementMemberRepository.findAllByPlacement(companyPlacementRepository.getReferenceById(source.id.toLong()))
            .map {
                CompanyPlacementMember(
                    id = it.id,
                    placementId = source.id,
                    account = Account(
                        id = it.member.account.id,
                        username = it.member.account.username,
                    )
                )
            }
    }

    @Transactional
    @SchemaMapping
    fun company(source: CompanyPlacement): Company {
        val company = companyRepository.findByIdOrNull(source.companyId)!!
        return Company(
            id = company.id,
            codename = company.codename,
            title = company.title,
        )
    }


}