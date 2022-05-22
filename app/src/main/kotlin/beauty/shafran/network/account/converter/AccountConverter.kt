package beauty.shafran.network.account.converter

import beauty.shafran.network.account.data.Account
import beauty.shafran.network.account.entity.AccountDataEntity
import beauty.shafran.network.account.entity.AccountEntity

interface AccountConverter {

    fun buildAccount(
        accountEntity: AccountEntity,
        accountDataEntity: AccountDataEntity,
    ): Account

}