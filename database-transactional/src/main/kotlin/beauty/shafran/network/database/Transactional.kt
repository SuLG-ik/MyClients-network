package beauty.shafran.network.database

import kotlinx.coroutines.Deferred

interface SetupTransactional {

    fun setup(statement: () -> Unit)

}

interface Transactional {

    suspend fun <T> withSuspendedTransaction(
        statement: suspend TransactionalScope.() -> T,
    ): T

}

suspend operator fun <T> Transactional.invoke(
    statement: suspend TransactionalScope.() -> T,
) = withSuspendedTransaction(statement)


interface TransactionalScope {

    suspend fun <T> transactionAsync(statement: suspend TransactionalScope.() -> T): Deferred<T>

}

