package beauty.shafran.network.accounts.route

import beauty.shafran.network.accounts.converter.AccountConverter
import beauty.shafran.network.accounts.repository.AccountRepository
import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.GetAccountRequest
import beauty.shafran.network.accounts.data.GetAccountResponse
import beauty.shafran.network.database.Transactional
import beauty.shafran.network.database.invoke

internal class AccountsRouterImpl(
    private val accountRepository: AccountRepository,
    private val converter: AccountConverter,
    private val transactional: Transactional,
) : AccountsRouter {

    override suspend fun getAccount(request: GetAccountRequest, account: AuthorizedAccount): GetAccountResponse {
        val id = request.accountId ?: AccountId(account.accountId)
        return transactional {
            val (accountEntity, accountData) = accountRepository.findAccountAndData(id)
            GetAccountResponse(
                account = converter.toAccount(accountEntity, accountData)
            )
        }
    }
}