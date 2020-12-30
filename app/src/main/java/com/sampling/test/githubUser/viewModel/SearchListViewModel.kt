package com.sampling.test.githubUser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampling.test.githubUser.Other.NO_NETWORK
import com.sampling.test.githubUser.api.ApiConfig.requestApi
import com.sampling.test.githubUser.data.ListData
import com.sampling.test.githubUser.data.UserListData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchListViewModel : ViewModel() {

    val userLists = MutableLiveData<ArrayList<UserListData>>()
    val connectionStatus = MutableLiveData<String>()

    fun setSearchList(username: String) {
        requestApi()
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