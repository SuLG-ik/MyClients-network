package beauty.shafran.network.accounts.route

import beauty.shafran.network.accounts.repositories.AccountDataEntityRepository
import beauty.shafran.network.accounts.repositories.AccountRepository
import beauty.shafran.network.auth.AuthorizedAccount
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

class AccountsQuery(
    val account: Account? = null,
)

class Account(
    val id: Long,
    val username: String,
)

class AccountData(
    val name: String = "",
)

@Controller
private class AccountsRouter(
    private val accountsRepository: AccountRepository,
    private val accountDataEntityRepository: AccountDataEntityRepository,
) {

    @QueryMapping
    fun accounts(): AccountsQuery {
        return AccountsQuery()
    }

    @SchemaMapping
    @Transactional
    fun account(source: AccountsQuery, @Argument("id") id: String?, @Argument("nickname") nickname: String?): Account {
        val account = when {
            !id.isNullOrEmpty() -> accountsRepository.findByIdOrNull(id.toLong())
            !nickname.isNullOrEmpty() -> accountsRepository.findByUsername(nickname)
            else -> {
                val authorized = AuthorizedAccount.get()
                accountsRepository.findByIdOrNull(authorized.accountId)
            }
        }!!
        return Account(
            id = account.id,
            username = account.username
        )
    }

    @SchemaMapping
    @Transactional
    fun data(source: Account): AccountData {
        val data = accountDataEntityRepository.findByAccount(accountsRepository.getReferenceById(source.id))
        return AccountData(
            name = data.name
        )
    }
}