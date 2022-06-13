package beauty.shafran.network.auth.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.auth.data.AccountPassword
import beauty.shafran.network.database.TransactionalScope

interface AuthPasswordRepository {

    context (TransactionalScope) suspend fun matchPassword(accountId: AccountId, rawPassword: AccountPassword): Boolean


    context (TransactionalScope) suspend fun setPassword(accountId: AccountId, rawPassword: AccountPassword)


}