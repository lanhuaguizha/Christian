package com.christian.data.source.remote

import com.christian.data.Nav
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.http.HTTP
import retrofit2.http.Path


interface NavService {

    @HTTP(method = "GET", path = "nav", hasBody = false)
    fun getBlog(@Path("id") id: Int): Call
}