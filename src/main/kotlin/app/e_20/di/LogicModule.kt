package app.e_20.di

import app.e_20.core.logic.ObjectMapper
import app.e_20.core.logic.PasswordEncoder
import app.e_20.core.logic.TokenGenerator
import app.e_20.core.logic.typedId.IdGenerator
import app.e_20.core.logic.typedId.impl.IxIdGenerator
import app.e_20.core.logic.typedId.impl.IxIntIdGenerator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val logicModule = module {
    single(createdAtStart = true) {
        ObjectMapper()
    }

    single(createdAtStart = true) {
        PasswordEncoder()
    }

    single(createdAtStart = true) {
        TokenGenerator()
    }

    single<IdGenerator>(named("uuid")) { IxIdGenerator() }
    single<IdGenerator>(named("int")) { IxIntIdGenerator() }
}