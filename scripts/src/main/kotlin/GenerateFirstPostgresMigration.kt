import app.e20.config.PostgresConfig
import app.e20.config.core.ConfigurationManager
import app.e20.config.core.ConfigurationReader
import app.e20.data.sources.db.schemas.user.PasswordResetTable
import app.e20.data.sources.db.schemas.user.UsersTable
import core.createScriptOutputsFolderIfNotExisting
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

private const val DB_DRIVER = "org.postgresql.Driver"

/**
 * Script that generates the first migration for the database schema
 * Result file should be put in /resources/db/migration/V1__create_db.sql
 */
fun main() {
    ConfigurationManager("app.e_20.config", ConfigurationReader::read).initialize()

    Database.connect(
        url = PostgresConfig.url,
        driver = DB_DRIVER,
        user = PostgresConfig.user,
        password = PostgresConfig.password
    )

    val statements = transaction {
        SchemaUtils.createStatements(
            UsersTable,
            PasswordResetTable,
        )
    }

    val folder = createScriptOutputsFolderIfNotExisting()
    val file = File(folder, "V1__create_db.sql")
    file.writeText(statements.joinToString("\n") { "$it;"} )
}