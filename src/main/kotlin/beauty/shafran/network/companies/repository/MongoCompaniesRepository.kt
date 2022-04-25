package beauty.shafran.network.companies.repository

import beauty.shafran.network.BusinessNotExists
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.companies.entity.AccessScope
import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.entity.CompanyEntityData
import beauty.shafran.network.companies.entity.CompanyMemberEntity
import beauty.shafran.network.utils.MongoTransactional
import beauty.shafran.network.utils.isDocumentExists
import beauty.shafran.network.utils.toIdSecure
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.litote.kmongo.and
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.springframework.stereotype.Repository

@Repository
class MongoCompaniesRepository(
    private val accountsRepository: AccountsRepository,
    private val mongoTransactional: MongoTransactional,
    private val businessesCollection: CoroutineCollection<CompanyEntity>,
    private val membersCollection: CoroutineCollection<CompanyMemberEntity>,
) : CompaniesRepository {

    override suspend fun throwIfBusinessNotExists(businessId: String) {
        if (!businessesCollection.isDocumentExists(CompanyEntity::id eq businessId.toIdSecure("businessId"))) {
            throw BusinessNotExists(businessId)
        }
    }

    override suspend fun findAccountMembersOfAccount(accountId: String): List<CompanyMemberEntity> {
        accountsRepository.throwIfAccountNotExistsOrDeactivated(accountId)
        return membersCollection.find(CompanyMemberEntity::accountId eq accountId).toList()
    }

    override suspend fun findCompanyById(companyId: String): CompanyEntity {
        return businessesCollection.findOneById(companyId.toIdSecure<CompanyEntity>("businessId"))
            ?: throw BusinessNotExists(companyId)
    }

    override suspend fun isAccessedForCompany(
        accessScope: AccessScope,
        accountId: String,
        businessId: String,
    ): Boolean {
        throwIfBusinessNotExists(businessId)
        accountsRepository.throwIfAccountNotExistsOrDeactivated(accountId)
        return membersCollection.isDocumentExists(
            and(
                CompanyMemberEntity::rawAccessedScopes contains accessScope,
                CompanyMemberEntity::accountId eq accountId,
                CompanyMemberEntity::companyId eq businessId
            )
        )
    }

    override suspend fun findCompanyForAccount(accountId: String): List<CompanyEntity> {
        return coroutineScope {
            accountsRepository.throwIfAccountNotExistsOrDeactivated(accountId)
            membersCollection.find(CompanyMemberEntity::accountId eq accountId).toList().map {
                async { businessesCollection.findOneById(it.companyId.toIdSecure<CompanyEntity>("businessId")) }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun createBusiness(data: CompanyEntityData): CompanyEntity {
        val companyEntity = CompanyEntity(data = data)
        businessesCollection.insertOne(companyEntity)
        return companyEntity
    }


}