package beauty.shafran.network.auth.repository

import beauty.shafran.network.auth.entity.AccountEntity
import org.springframework.stereotype.Repository

@Repository
class MongoAuthRepository : AuthRepository {
    override fun findAccountByUsername(username: String): AccountEntity {
        TODO("Not yet implemented")
    }
}