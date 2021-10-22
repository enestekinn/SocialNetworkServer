package com.enestekin

import io.ktor.application.*
import com.enestekin.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


// Entry point to main server
@Suppress("unused")
fun Application.module() {
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
}
