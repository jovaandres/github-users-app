package com.sampling.test.githubUser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampling.test.githubUser.Other.BASE_URL
import com.sampling.test.githubUser.Other.NO_NETWORK
import com.sampling.test.githubUser.Other.getClient
import com.sampling.test.githubUser.api.ApiService
import com.sampling.test.githubUser.data.ListData
import com.sampling.test.githubUser.data.UserListData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchListViewModel : ViewModel() {

    val userLists = MutableLiveData<ArrayList<UserListData>>()
    val connectionStatus = MutableLiveData<String>()

    fun setSearchList(username: String) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
            .create(ApiService::class.java)
            .getSearchUser(username)
            .enqueue(object : Callback<ListData> {
                override fun onFailure(call: Call<ListData>, t: Throwable) {
                    connectionStatus.postValue(NO_NETWORK)
                }

                override fun onResponse(call: Call<ListData>, response: Response<ListData>) {
                    userLists.postValue(response.body()?.items)
                }
            })
    }

    //get putted data
    fun getSearchList(): LiveData<ArrayList<UserListData>> = userLists
    fun getConnectionStatus(): LiveData<String> = connectionStatus
}