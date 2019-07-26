package com.christian.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class NavViewModel : ViewModel(), AnkoLogger {

    fun loadImage() {
        viewModelScope.launch {
            val b = withContext(viewModelScope.coroutineContext) {
                loadImageAsync()
            }
            info { b }
        }
    }

    suspend fun loadImageAsync(): Boolean {
        // Asynchronous Programming
        withContext(Dispatchers.IO) {
            Thread.sleep(3000)
        }
        return true
    }
}