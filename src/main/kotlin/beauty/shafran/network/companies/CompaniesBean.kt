package beauty.shafran.network.companies

import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.entity.CompanyMemberEntity
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompaniesBean {

    @Bean
    fun getBusinessCollection(database: CoroutineDatabase): CoroutineCollection<CompanyEntity> {
        return database.getCollection("companies")
    }

    @Bean
    fun getBusinessMemberCollection(database: CoroutineDatabase): CoroutineCollection<CompanyMemberEntity> {
        return database.getCollection("companies_member")
    }

}