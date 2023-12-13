package app.e_20.di

import app.e_20.core.logic.ObjectMapper
import app.e_20.core.logic.PasswordEncoder
import app.e_20.core.logic.TokenGenerator
import app.e_20.core.logic.typedId.IdGenerator
import app.e_20.core.logic.typedId.impl.IxIdGenerator
import app.e_20.core.logic.typedId.impl.IxIntIdGenerator
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val logicModule = module {

    singleOf(::ObjectMapper) {
        createdAtStart()
    }

    singleOf(::PasswordEncoder) {
        createdAtStart()
    }

    singleOf(::TokenGenerator) {
        createdAtStart()
    }

    single<IdGenerator>(named("uuid")) { IxIdGenerator() }
    single<IdGenerator>(named("int")) { IxIntIdGenerator() }
}