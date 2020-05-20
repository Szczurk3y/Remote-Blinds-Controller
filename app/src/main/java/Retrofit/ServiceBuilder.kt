package Retrofit

import retrofit2.Retrofit

class ServiceBuilder(val BASE_URL: String) {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http:/${this.BASE_URL}:10107/")
        .build()

    fun getService(): Service {
        return retrofit.create(Service::class.java)
    }
}