package app.e_20.di

import app.e_20.core.logic.ObjectMapper
import app.e_20.core.logic.PasswordEncoder
import app.e_20.core.logic.TokenGenerator
import app.e_20.core.logic.typedId.IdGenerator
import app.e_20.core.logic.typedId.impl.IxIdGenerator
import app.e_20.core.logic.typedId.impl.IxIntIdGenerator
import app.e_20.data.daos.auth.PasswordResetDao
import app.e_20.data.daos.auth.UserSessionDao
import app.e_20.data.daos.auth.impl.PasswordResetDaoImpl
import app.e_20.data.daos.auth.impl.UserSessionDaoCacheImpl
import app.e_20.data.daos.user.UserDao
import app.e_20.data.daos.user.impl.UserDaoImpl
import app.e_20.data.sources.cache.cm.UserSessionCM
import app.e_20.data.sources.cache.cm.impl.UserSessionCMImpl
import app.e_20.data.sources.db.dbi.user.PasswordResetDBI
import app.e_20.data.sources.db.dbi.user.UserDBI
import app.e_20.data.sources.db.dbi.user.impl.PasswordResetDBIImpl
import app.e_20.data.sources.db.dbi.user.impl.UserDBIImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userModule = module {
    // User dao
    singleOf(::UserDBIImpl) {
        bind<UserDBI>()
        createdAtStart()
    }

    single<UserDao>(createdAtStart = true) {
        UserDaoImpl(get())
    }


    // User auth session dao
    single<UserSessionCM>(createdAtStart = true) {
        UserSessionCMImpl(get(), get())
    }

    single<UserSessionDao>(createdAtStart = true) {
        UserSessionDaoCacheImpl(get())
    }


    // User password reset dao
    single<PasswordResetDBI>(createdAtStart = true) {
        PasswordResetDBIImpl(get())
    }

    single<PasswordResetDao>(createdAtStart = true) {
        PasswordResetDaoImpl(get(), get(), get())
    }
}