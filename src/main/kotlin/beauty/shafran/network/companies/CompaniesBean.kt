package beauty.shafran.network.companies

import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.entity.CompanyMemberEntity
import beauty.shafran.network.companies.entity.CompanyStationEntity
import beauty.shafran.network.companies.entity.CompanyStationMemberEntity
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

@Module
class CompaniesModule {

    @Single
    @Named(CompanyEntity.collectionName)
    fun getBusinessCollection(database: CoroutineDatabase): CoroutineCollection<CompanyEntity> {
        return database.getCollection(CompanyEntity.collectionName)
    }

    @Single
    @Named(CompanyMemberEntity.collectionName)
    fun getBusinessMemberCollection(database: CoroutineDatabase): CoroutineCollection<CompanyMemberEntity> {
        return database.getCollection(CompanyMemberEntity.collectionName)
    }

    @Single
    @Named(CompanyStationEntity.collectionName)
    fun getCompanyStations(database: CoroutineDatabase): CoroutineCollection<CompanyStationEntity> {
        return database.getCollection(CompanyStationEntity.collectionName)
    }


    @Single
    @Named(CompanyStationMemberEntity.collectionName)
    fun getCompanyStationMemberEntity(database: CoroutineDatabase): CoroutineCollection<CompanyStationMemberEntity> {
        return database.getCollection(CompanyStationMemberEntity.collectionName)
    }
}