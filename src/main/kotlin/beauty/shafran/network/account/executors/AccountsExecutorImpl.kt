package beauty.shafran.network.account.executors

import beauty.shafran.network.account.data.Account
import beauty.shafran.network.account.data.AccountData
import beauty.shafran.network.account.data.GetAttachedAccountRequest
import beauty.shafran.network.account.data.GetAttachedAccountResponse
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.data.AuthorizedAccount
import org.koin.core.annotation.Single

@Single
class AccountsExecutorImpl(
    private val accountsRepository: AccountsRepository,
) : AccountsExecutor {

    override suspend fun getAccount(
        request: GetAttachedAccountRequest,
        account: AuthorizedAccount,
    ): GetAttachedAccountResponse {
        val accountEntity = accountsRepository.findAccountById(account.accountId)
        return GetAttachedAccountResponse(
            Account(
                id = accountEntity.id.toString(),
                data = AccountData(
                    username = accountEntity.data.username,
                    name = accountEntity.data.name,
                )
            )
        )
    }

}