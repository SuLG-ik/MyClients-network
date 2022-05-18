package beauty.shafran.network.admin.company.execute

import beauty.shafran.network.companies.data.CreateCompanyRequest
import beauty.shafran.network.companies.data.CreateCompanyResponse

interface AdminCompanyExecutor {

    suspend fun createCompany(request: CreateCompanyRequest): CreateCompanyResponse

}