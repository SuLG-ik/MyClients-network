package beauty.shafran.network.admin.company.execute

import beauty.shafran.network.companies.data.CreateCompanyRequest
import beauty.shafran.network.companies.data.CreateCompanyResponse
import beauty.shafran.network.companies.entity.CompanyEntityData
import beauty.shafran.network.companies.repository.CompaniesRepository
import beauty.shafran.network.utils.Transactional
import beauty.shafran.network.utils.invoke

class AdminCompanyExecutorImpl(
    private val transactional: Transactional,
    private val companiesRepository: CompaniesRepository,
) : AdminCompanyExecutor {
    override suspend fun createCompany(
        request: CreateCompanyRequest,
    ): CreateCompanyResponse {
        return transactional {
            val company = companiesRepository.run {
                createCompany(
                    CompanyEntityData(
                        title = request.data.companyTitle,
                        codename = request.data.companyCodeName,
                    ),
                )
            }
            CreateCompanyResponse(
                company = TODO()
            )
        }
    }
}