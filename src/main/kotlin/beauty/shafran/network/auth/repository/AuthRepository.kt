package beauty.shafran.network.auth.repository

import beauty.shafran.network.auth.entity.AccountEntity

interface AuthRepository {

    fun findAccountByUsername(username: String): AccountEntity

}