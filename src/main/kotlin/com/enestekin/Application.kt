package com.enestekin

import com.enestekin.di.mainModule
import io.ktor.application.*
import com.enestekin.plugins.*
import org.koin.ktor.ext.Koin


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)



// Entry point to main server
@Suppress("unused")
fun Application.module() {

    configureSecurity()
    configureRouting()
    configureHTTP()
    configureMonitoring()
    configureSerialization()

    install(Koin) {
        modules(mainModule)
    }
}



