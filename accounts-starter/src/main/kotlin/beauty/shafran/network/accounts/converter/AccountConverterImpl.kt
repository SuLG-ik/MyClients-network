package beauty.shafran.network.accounts.converter

import beauty.shafran.network.accounts.tables.AccountDataEntity
import beauty.shafran.network.accounts.tables.AccountEntity
import beauty.shafran.network.accounts.data.Account
import beauty.shafran.network.accounts.data.AccountData
import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountUsername

class AccountConverterImpl : AccountConverter {
    override fun toAccount(accountEntity: AccountEntity, accountDataEntity: AccountDataEntity): Account {
        return Account(
            id = AccountId(accountEntity.id),
            username = AccountUsername(accountEntity.username),
            data = AccountData(
                name = accountDataEntity.name
            )
        )
    }
}