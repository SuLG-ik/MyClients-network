package beauty.shafran.network.companies.route

import beauty.shafran.network.accounts.repositories.AccountRepository
import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.companies.entities.*
import beauty.shafran.network.companies.repository.*
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional


class CompaniesMutation(
    var addCompany: Company? = null,
    var addPlacement: CompanyPlacement? = null,
)

class CreateCompanyInput(
    var codename: String,
    var title: String = codename,
)

class AddPlacementInput(
    var companyId: Long,
    var codename: String,
    var title: String,
    var addToMembers: Boolean,
)

@Controller
class MutationCompanyController(
    private val companyRepository: CompanyRepository,
    private val companyOwnerRepository: CompanyOwnerRepository,
    private val companyMemberRepository: CompanyMemberRepository,
    private val accountRepository: AccountRepository,
    private val companyPlacementRepository: CompanyPlacementRepository,
    private val companyPlacementMemberRepository: CompanyPlacementMemberRepository,
) {

    @MutationMapping()
    fun companies(): CompaniesMutation {
        return CompaniesMutation()
    }

    @SchemaMapping(typeName = "CompaniesMutation")
    @Transactional
    fun addCompany(
        @Argument input: CreateCompanyInput,
        @AuthenticationPrincipal account: AuthorizedAccount,
    ): Company {
        val company = companyRepository.save(CompanyEntity(codename = input.codename, title = input.title))
        val member = companyMemberRepository.save(
            CompanyMemberEntity(
                company = company,
                account = accountRepository.getReferenceById(account.accountId)
            )
        )
        companyOwnerRepository.save(CompanyOwnerEntity(member = member, company = company))
        return Company(
            id = company.id,
            codename = input.codename,
            title = input.title
        )
    }

    @SchemaMapping(typeName = "CompaniesMutation")
    @Transactional
    fun addPlacement(
        @Argument input: AddPlacementInput,
        @AuthenticationPrincipal account: AuthorizedAccount,
    ): CompanyPlacement {
        val company = companyRepository.getReferenceById(input.companyId)
        val placement = companyPlacementRepository.save(
            CompanyPlacementEntity(
                company = company,
                title = input.title,
                codename = input.codename
            )
        )
        if (input.addToMembers) {
            val member = companyMemberRepository.findByCompanyAndAccount(
                company,
                accountRepository.getReferenceById(account.accountId)
            )
            companyPlacementMemberRepository.save(
                CompanyPlacementMemberEntity(
                    member = member,
                    placement = placement
                )
            )
        }
        return CompanyPlacement(
            id = placement.id,
            companyId = input.companyId,
            codename = input.codename,
            data = CompanyPlacementData(title = input.title),
        )
    }

}