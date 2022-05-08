package beauty.shafran.network.companies.repository

import beauty.shafran.BusinessNotExists
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.companies.entity.*
import beauty.shafran.network.utils.isDocumentExists
import beauty.shafran.network.utils.toIdSecure
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.and
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

@Single
class MongoCompaniesRepository(
    private val accountsRepository: AccountsRepository,
    @Named(CompanyEntity.collectionName)
    private val businessesCollection: CoroutineCollection<CompanyEntity>,
    @Named(CompanyMemberEntity.collectionName)
    private val membersCollection: CoroutineCollection<CompanyMemberEntity>,
    @Named(CompanyStationEntity.collectionName)
    private val stationsEntity: CoroutineCollection<CompanyStationEntity>,
) : CompaniesRepository {

    override suspend fun throwIfBusinessNotExists(businessId: String) {
        if (!businessesCollection.isDocumentExists(CompanyEntity::id eq businessId.toIdSecure("businessId"))) {
            throw BusinessNotExists(businessId)
        }
    }

    override suspend fun findAccountMembersForAccount(accountId: String): List<CompanyMemberEntity> {
        accountsRepository.throwIfAccountNotExistsOrDeactivated(accountId)
        return membersCollection.find(CompanyMemberEntity::accountId eq accountId).toList()
    }

    override suspend fun findCompanyById(companyId: String): CompanyEntity {
        return businessesCollection.findOneById(companyId.toIdSecure("businessId"))
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
                async { businessesCollection.findOneById(it.companyId.toIdSecure("businessId")) }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun createCompany(data: CompanyEntityData, ownerAccountId: String): CompanyEntity {
        accountsRepository.throwIfAccountNotExistsOrDeactivated(ownerAccountId)
        val companyEntity = CompanyEntity(data = data)
        val member = CompanyMemberEntity(
            companyId = companyEntity.id.toString(),
            accountId = ownerAccountId,
            rawAccessedScopes = getOwnerAccessScopes(),
        )
        businessesCollection.insertOne(companyEntity)
        membersCollection.insertOne(member)
        return companyEntity
    }

    suspend fun createStation(data: CompanyStationEntityData, location: CompanyStationEntityLocation, companyId: String): CompanyStationEntity {
        val companyStation = CompanyStationEntity(
            companyId = companyId,
            data= data,
            location = location,
        )
        stationsEntity.save(companyStation)
        return companyStation
    }

    private fun getOwnerAccessScopes(): List<AccessScope> {
        return listOf(
            AccessScope.MEMBER_ACCESS_PRIORITY,
            AccessScope.EMPLOYEES_READ,
            AccessScope.EMPLOYEES_REMOVE,
            AccessScope.EMPLOYEES_ADD,
            AccessScope.EMPLOYEES_EDIT,
            AccessScope.SERVICES_READ,
            AccessScope.SERVICES_REMOVE,
            AccessScope.SERVICES_ADD,
            AccessScope.SERVICES_EDIT,
            AccessScope.SESSIONS_READ,
            AccessScope.SESSIONS_REMOVE,
            AccessScope.SESSIONS_ADD,
            AccessScope.SESSIONS_USE,
            AccessScope.SESSIONS_EDIT,
            AccessScope.CUSTOMERS_READ,
            AccessScope.CUSTOMERS_REMOVE,
            AccessScope.CUSTOMERS_ADD,
            AccessScope.CUSTOMERS_EDIT,
            AccessScope.COMPANY_READ,
            AccessScope.COMPANY_EDIT,
            AccessScope.COMPANY_MEMBERS_READ,
            AccessScope.COMPANY_MEMBERS_ADD,
            AccessScope.COMPANY_MEMBERS_REMOVE,
            AccessScope.COMPANY_MEMBERS_EDIT,
        )
    }

}