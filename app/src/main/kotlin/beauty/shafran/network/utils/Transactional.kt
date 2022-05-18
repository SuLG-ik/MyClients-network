package beauty.shafran.network.utils

import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.koin.core.annotation.Single

interface Transactional {

    suspend fun <T> withSuspendedTransaction(
        statement: suspend TransactionalScope.() -> T,
    ): T

}

suspend operator fun <T> Transactional.invoke(
    statement: suspend TransactionalScope.() -> T,
) = withSuspendedTransaction(statement)

class DelegateTransactionalScope : TransactionalScope {
    override suspend fun <T> transactionAsync(statement: suspend TransactionalScope.() -> T): Deferred<T> {
        return suspendedTransactionAsync<T>(statement = { statement() })
    }
}

interface TransactionalScope {

    suspend fun <T> transactionAsync(statement: suspend TransactionalScope.() -> T): Deferred<T>
}

@Single
class ExposedTransactional(
    private val db: Database,
) : Transactional {

    override suspend fun <T> withSuspendedTransaction(statement: suspend TransactionalScope.() -> T): T {
        return newSuspendedTransaction(db = db, statement = {
            statement.invoke(DelegateTransactionalScope())
        })
    }
}