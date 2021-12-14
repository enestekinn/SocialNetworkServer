package com.enestekin.di

import com.enestekin.data.repository.activity.ActivityRepository
import com.enestekin.data.repository.activity.ActivityRepositoryImpl
import com.enestekin.data.repository.comment.CommentRepository
import com.enestekin.data.repository.comment.CommentRepositoryImpl
import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.repository.follow.FollowRepositoryImpl
import com.enestekin.data.repository.likes.LikeRepository
import com.enestekin.data.repository.likes.LikeRepositoryImpl
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.repository.post.PostRepositoryImpl
import com.enestekin.data.repository.skill.SkillRepository
import com.enestekin.data.repository.skill.SkillRepositoryImpl
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.data.repository.user.UserRepositoryImpl
import com.enestekin.service.PostService
import com.enestekin.service.*
import com.enestekin.util.Constants
import com.google.gson.Gson
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {

    single {
        println("Okundu")
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)



    }

    single<UserRepository> {
        UserRepositoryImpl(get())

    }
    single<FollowRepository> {
        FollowRepositoryImpl(get()) }

    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }

    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }

    single<SkillRepository> {
        SkillRepositoryImpl(get())
    }


    single { UserService(get(),get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get(),get(),get()) }
    single { CommentService(get()) }
    single { ActivityService(get(),get(),get()) }
    single { SkillService(get()) }

    single { Gson() }


}