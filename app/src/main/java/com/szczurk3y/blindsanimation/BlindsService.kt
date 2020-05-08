package com.szczurk3y.blindsanimation

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BlindsService {
    @GET("/shouldbe")
    fun shouldBe(
        @Query("PERCENT") percent: String
    ): Call<ResponseBody>
}