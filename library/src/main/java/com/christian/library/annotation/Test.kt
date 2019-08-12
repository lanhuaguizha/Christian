package com.christian.library.annotation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
//    withContextCalculator(1)
//    withContextCalculator(1)
//    withContextCalculator(1)
    launch { withContextCalculator(1) }
    launch { withContextCalculator(1) }
    launch { withContextCalculator(1) }
    val endTime = System.currentTimeMillis()

    println("runtime is ${endTime - startTime}")

//    launch { withContextCalculator(1) }
//    launch { withContextCalculator(1) }
//    launch { withContextCalculator(1) }
}

suspend fun withContextCalculator(num: Int) {
    val result1 = withContext(Dispatchers.Default) {
        delay(1000)
        return@withContext num
    }

    val result2 = withContext(Dispatchers.Default) {
        delay(1000)
        return@withContext num
    }

    val result3 = withContext(Dispatchers.Default) {
        delay(1000)
        return@withContext num
    }

    println(result1 + result2 + result3)
}

class MainViewModel : ViewModel() {

    fun main() {

        viewModelScope.launch {
            withContextCalculator(1)
        }
    }
}