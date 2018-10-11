package com.christian.nav

import kotlinx.coroutines.experimental.coroutineScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

fun handleURL() {
}

fun main(args: Array<String>) = runBlocking {
    // this: CoroutineScope
    launch {
        delay(200L)
        println("Task from runBlocking")
    }

    coroutineScope {
        // Creates a new coroutine scope
        launch {
            delay(500L)
            println("Task from coroutineScope")
        }

        delay(100L)
        println("coroutineScope is over") // This line will be printed before nested launch
    }

    println("runBlocking is over") // This line is not printed until nested launch completes
}

