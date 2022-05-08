package beauty.shafran.network.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.auth.jwt.JwtConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

@Module
class AuthModule {

    @Single
    @Named(AccountSessionEntity.collectionName)
    fun getAccountsSessionsCollection(database: CoroutineDatabase): CoroutineCollection<AccountSessionEntity> {
        return database.getCollection(AccountSessionEntity.collectionName)
    }

    @Single
    fun hasher(): BCrypt.Hasher {
        return BCrypt.withDefaults()
    }

    @Single
    fun verifier(): BCrypt.Verifyer {
        return BCrypt.verifyer()
    }

    @Single
    fun jwtConfig(): JwtConfig {
        return JwtConfig()
    }


}