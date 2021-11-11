package com.enestekin.plugins

import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*



fun Application.configureMonitoring() {
    install(CallLogging) { // log whenever client makes a call   then you see in a console
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

}


