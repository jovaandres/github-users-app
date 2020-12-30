package com.sampling.test.githubUser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampling.test.githubUser.Other.NO_NETWORK
import com.sampling.test.githubUser.api.ApiConfig.requestApi
import com.sampling.test.githubUser.data.UserDetailData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    val userDetail = MutableLiveData<UserDetailData>()
    val connectionStatus = MutableLiveData<String>()

    fun setDetailUser(username: String) {
       requestApi()
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