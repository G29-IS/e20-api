package app.e_20.di

import app.e_20.core.logic.typedId.IdGenerator
import app.e_20.core.logic.typedId.impl.IxIdGenerator
import app.e_20.core.logic.typedId.impl.IxIntIdGenerator
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("app.e_20.core.logic")
class LogicModule()