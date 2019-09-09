package com.christian.annotation

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val time = measureTimeMillis {
        launch {
            calThingsAsync()
        }
    }

    println("runtime-from thread ${Thread.currentThread().name} is $time")
}

private suspend fun calThingsAsync() {
    coroutineScope {
        val r1 = async(Dispatchers.IO) { calThings(1) }
        val r2 = async(Dispatchers.IO) { calThings(2) }
        val r3 = async(Dispatchers.IO) { calThings(3) }
        println("sum-from thread ${Thread.currentThread().name}, ${r1.await() + r2.await() + r3.await()}")
    }
}

suspend fun calThings(i: Int): Int {
    delay(1000)
    println("r$i-from thread ${Thread.currentThread().name}, $i")
    return i
}