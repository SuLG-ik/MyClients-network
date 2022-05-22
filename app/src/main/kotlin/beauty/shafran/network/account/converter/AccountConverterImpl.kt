package beauty.shafran.network.account.converter

import beauty.shafran.network.account.data.Account
import beauty.shafran.network.account.data.AccountData
import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.entity.AccountDataEntity
import beauty.shafran.network.account.entity.AccountEntity
import org.koin.core.annotation.Single


@Single
class AccountConverterImpl : AccountConverter {
    override fun buildAccount(accountEntity: AccountEntity, accountDataEntity: AccountDataEntity): Account {
        return Account(
            id = AccountId(accountEntity.id),
            data = AccountData(
                name = accountEntity.username,
                username = accountDataEntity.name,
            )
        )
    }
}