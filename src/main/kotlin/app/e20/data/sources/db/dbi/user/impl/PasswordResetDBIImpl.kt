package app.e20.data.sources.db.dbi.user.impl

import app.e20.core.logic.TokenGenerator
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.PasswordResetData
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.user.PasswordResetDBI
import app.e20.data.sources.db.schemas.user.PasswordResetEntity
import app.e20.data.sources.db.schemas.user.PasswordResetTable
import app.e20.data.sources.db.schemas.user.UsersTable
import app.e20.data.sources.db.toEntityId
import app.e20.data.sources.db.toIxId
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class PasswordResetDBIImpl(
    private val tokenGenerator: TokenGenerator
) : PasswordResetDBI {
    private fun PasswordResetEntity.fromDto(passwordResetData: PasswordResetData) {
        token = passwordResetData.token
        user = passwordResetData.userId.toEntityId(UsersTable)
        createdAt = passwordResetData.createdAt
        expiresAt = passwordResetData.expireAt
    }

    private fun PasswordResetEntity.toDto() = PasswordResetData(
        token = token,
        userId = user.toIxId(),
        createdAt = createdAt,
        expireAt = expiresAt
    )

    override suspend fun count(id: IxId<UserData>): Long = dbQuery {
        PasswordResetEntity.count(PasswordResetTable.user eq id.toEntityId(UsersTable))
    }

    override suspend fun save(passwordResetData: PasswordResetData) {
        dbQuery {
            PasswordResetEntity.new {
                fromDto(passwordResetData)
            }
        }
    }

    override suspend fun get(token: String): PasswordResetData? = dbQuery {
        PasswordResetEntity
            .find { PasswordResetTable.token eq tokenGenerator.hashToken(token) }
            .limit(1)
            .firstOrNull()
            ?.toDto()
    }

    override suspend fun deleteAll(id: IxId<UserData>) {
        dbQuery {
            PasswordResetTable.deleteWhere { user eq id.toEntityId(UsersTable) }
        }
    }

}