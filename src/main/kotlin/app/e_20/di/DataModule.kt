package app.e_20.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [LogicModule::class, ClientModule::class])
@ComponentScan("app.e_20.data")
class DataModule