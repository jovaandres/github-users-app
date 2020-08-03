package com.sampling.test.githubUser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampling.test.githubUser.Other.BASE_URL
import com.sampling.test.githubUser.Other.NO_NETWORK
import com.sampling.test.githubUser.Other.getClient
import com.sampling.test.githubUser.api.ApiService
import com.sampling.test.githubUser.data.UserDetailData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DetailUserViewModel : ViewModel() {

    val userDetail = MutableLiveData<UserDetailData>()
    val connectionStatus = MutableLiveData<String>()

    fun setDetailUser(username: String) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
            .create(ApiService::class.java)
            .getUserDetail(username)
            .enqueue(object : Callback<UserDetailData> {
                override fun onFailure(call: Call<UserDetailData>, t: Throwable) {
                    connectionStatus.postValue(NO_NETWORK)
                }

                override fun onResponse(
                    call: Call<UserDetailData>,
                    response: Response<UserDetailData>
                ) {
                    userDetail.postValue(response.body())
                }
            })
    }

    //get putted data
    fun getDetailUser(): LiveData<UserDetailData> = userDetail
    fun getConnectionStatus(): LiveData<String> = connectionStatus
}