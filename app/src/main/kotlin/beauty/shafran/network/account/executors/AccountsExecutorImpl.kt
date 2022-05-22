package beauty.shafran.network.account.executors

import beauty.shafran.network.account.converter.AccountConverter
import beauty.shafran.network.account.data.GetAttachedAccountRequest
import beauty.shafran.network.account.data.GetAttachedAccountResponse
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.utils.Transactional
import beauty.shafran.network.utils.invoke
import org.koin.core.annotation.Single

@Single
class AccountsExecutorImpl(
    private val accountsRepository: AccountsRepository,
    private val converter: AccountConverter,
    private val transactional: Transactional,
) : AccountsExecutor {

    override suspend fun getAccount(
        request: GetAttachedAccountRequest,
        account: AuthorizedAccount,
    ): GetAttachedAccountResponse {
        return transactional {
            val accountEntity = accountsRepository.run { findAccountById(account.accountId) }
            val data = accountsRepository.run { findAccountDataById(account.accountId) }
            GetAttachedAccountResponse(
                account = converter.buildAccount(accountEntity, data)
            )
        }
    }

}