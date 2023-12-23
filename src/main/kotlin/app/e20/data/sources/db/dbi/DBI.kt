package app.e20.data.sources.db.dbi

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Common interface for all database interactors
 */
interface DBI {
    /**
     * Executes a suspended transaction on the database
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(
            context = Dispatchers.IO,
        ) { block() }
}