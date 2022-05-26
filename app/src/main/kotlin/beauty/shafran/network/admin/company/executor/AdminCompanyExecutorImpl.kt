package beauty.shafran.network.admin.company.executor

import beauty.shafran.network.companies.converter.CompanyConverter
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CreateCompanyRequest
import beauty.shafran.network.companies.data.CreateCompanyResponse
import beauty.shafran.network.companies.repository.CompaniesRepository
import beauty.shafran.network.utils.Transactional
import beauty.shafran.network.utils.invoke

class AdminCompanyExecutorImpl(
    private val transactional: Transactional,
    private val converter: CompanyConverter,
    private val companiesRepository: CompaniesRepository,
) : AdminCompanyExecutor {
    override suspend fun createCompany(
        request: CreateCompanyRequest,
    ): CreateCompanyResponse {
        return transactional {
            val company = companiesRepository.createCompany(
                request = request.data,
                ownerAccountId = request.ownerAccountId,
            )
            val data = companiesRepository.findCompanyDataById(CompanyId(company.id))
            CreateCompanyResponse(
                company = converter.buildCompany(company, data)
            )
        }
    }
}