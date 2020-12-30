package com.sampling.test.githubUser.api

import com.sampling.test.githubUser.Other.TOKEN
import com.sampling.test.githubUser.data.FollowData
import com.sampling.test.githubUser.data.ListData
import com.sampling.test.githubUser.data.UserDetailData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("Authorization: token $TOKEN")
    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetailData>

    @Headers("Authorization: token $TOKEN")
    @GET("users/{username}/{type}")
    fun getUserFollow(@Path("username") username: String, @Path("type") type: String): Call<FollowData>

    @Headers("Authorization: token $TOKEN")
    @GET("search/users")
    fun getSearchUser(@Query("q") q: String): Call<ListData>

}