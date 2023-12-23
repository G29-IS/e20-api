package app.e20.core.clients

import app.e20.config.PostgresConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Single

private val log = KotlinLogging.logger {  }

/**
 * Client to interact with a Postgres database
 */
@Single(createdAtStart = true)
class PostgresClient {
    private val dbDriver = "org.postgresql.Driver"

    private val database = Database.connect(
        url = PostgresConfig.url,
        driver = dbDriver,
        user = PostgresConfig.user,
        password = PostgresConfig.password
    )

    @Suppress("UNUSED")
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(
            context = Dispatchers.IO,
        ) { block() }

    init {
        runMigrations()
    }

    /**
     * Runs all the migrations stored inside `src/resources/db/migration`
     */
    private fun runMigrations() {
        val flyway = Flyway.configure()
            .driver(dbDriver)
            .dataSource(PostgresConfig.url, PostgresConfig.user, PostgresConfig.password)
            .validateMigrationNaming(true)
            .load()
        try {
            flyway.info()
            flyway.migrate()
            log.info { "Flyway migration has finished" }
        } catch (e: Exception) {
            log.error(e) { "Exception running flyway migration" }
            throw e
        }
    }

    @Suppress("UNUSED")
    fun getDb() = database
}