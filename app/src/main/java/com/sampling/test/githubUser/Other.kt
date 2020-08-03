package com.sampling.test.githubUser

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object Other {
    const val BASE_URL = "https://api.github.com/"
    const val NO_NETWORK = "Unavailable"

    fun getClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}