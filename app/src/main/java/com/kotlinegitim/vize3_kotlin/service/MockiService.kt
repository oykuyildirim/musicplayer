package com.kotlinegitim.vize3_kotlin.service

import com.kotlinegitim.vize3_kotlin.Music
import retrofit2.Call
import retrofit2.http.GET

interface MockiService {
    @GET("f27fbefc-d775-4aee-8d65-30f76f1f7109")
    fun allMusic(): Call<Music>
}