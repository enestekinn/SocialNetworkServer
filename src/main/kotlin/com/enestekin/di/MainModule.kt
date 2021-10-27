package com.enestekin.di

import com.enestekin.repository.UserRepository
import com.enestekin.repository.UserRepositoryImpl
import com.enestekin.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo



val mainModule = module {

    single {

        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)


    }

    single<UserRepository> {

       UserRepositoryImpl(get())
    }


}