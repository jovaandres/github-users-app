package com.sampling.test.githubUser.api

import com.sampling.test.githubUser.data.FollowData
import com.sampling.test.githubUser.data.ListData
import com.sampling.test.githubUser.data.UserDetailData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetailData>

    @GET("users/{username}/{type}")
    fun getUserFollow(@Path("username") username: String, @Path("type") type: String): Call<FollowData>

    @GET("search/users")
    fun getSearchUser(@Query("q") q: String): Call<ListData>

}