package beauty.shafran.network.companies.converter

import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.entity.CompanyDataEntity
import beauty.shafran.network.companies.entity.CompanyEntity

interface CompanyConverter {

    fun buildCompany(companyEntity: CompanyEntity, companyDataEntity: CompanyDataEntity): Company

}