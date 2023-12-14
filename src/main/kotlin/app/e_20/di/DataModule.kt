package app.e_20.di

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
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@Module(includes = [LogicModule::class, ClientModule::class])
@ComponentScan("app.e_20.data")
class DataModule()