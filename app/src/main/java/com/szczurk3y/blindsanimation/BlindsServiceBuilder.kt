package com.szczurk3y.blindsanimation

import retrofit2.Retrofit
import java.net.InetAddress

class BlindsServiceBuilder(val BASE_URL: InetAddress) {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http:/${this.BASE_URL}:10107/")
        .build()

    fun getService(): BlindsService {
        return retrofit.create(BlindsService::class.java)
    }
}