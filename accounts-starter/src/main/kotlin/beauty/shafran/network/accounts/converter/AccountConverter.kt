package beauty.shafran.network.accounts.converter

import beauty.shafran.network.accounts.tables.AccountDataEntity
import beauty.shafran.network.accounts.tables.AccountEntity
import beauty.shafran.network.accounts.data.Account

interface AccountConverter  {

    fun toAccount(accountEntity: AccountEntity, accountDataEntity: AccountDataEntity): Account

}