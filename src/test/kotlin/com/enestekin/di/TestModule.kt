package com.enestekin.di


import com.enestekin.repository.user.FakeUserRepository
import org.koin.dsl.module

internal val testModule = module {

    single { FakeUserRepository() }
}