package beauty.shafran.network.companies.converter

import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.data.CompanyCodename
import beauty.shafran.network.companies.data.CompanyData
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.CompanyDataEntity
import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.utils.toMetaData

class CompanyConverterImpl : CompanyConverter {
    override fun buildCompany(companyEntity: CompanyEntity, companyDataEntity: CompanyDataEntity): Company {
        return Company(
            id = CompanyId(companyEntity.id),
            data = CompanyData(
                title = companyDataEntity.title,
            ),
            codename = CompanyCodename(companyEntity.codename),
            meta = companyEntity.creationDate.toMetaData()
        )
    }
}