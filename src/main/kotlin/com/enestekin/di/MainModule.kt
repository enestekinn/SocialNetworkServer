package com.enestekin.di

import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.repository.follow.FollowRepositoryImpl
import com.enestekin.data.repository.likes.LikeRepository
import com.enestekin.data.repository.likes.LikeRepositoryImpl
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.repository.post.PostRepositoryImpl
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.data.repository.user.UserRepositoryImpl
import com.enestekin.routes.PostService
import com.enestekin.service.FollowService
import com.enestekin.service.LikeService
import com.enestekin.service.UserService
import com.enestekin.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo



val mainModule = module {

    single {

        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)


    }

    single<UserRepository> { UserRepositoryImpl(get()) }

    single<FollowRepository> { FollowRepositoryImpl(get()) }

    single<PostRepository> {PostRepositoryImpl(get())  }

    single<LikeRepository> { LikeRepositoryImpl(get())  }

    single { UserService(get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get()) }


}