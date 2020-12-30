package com.sampling.test.githubUser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampling.test.githubUser.api.ApiConfig.requestApi
import com.sampling.test.githubUser.data.FollowData
import com.sampling.test.githubUser.data.UserFollowData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowsViewModel : ViewModel() {

    private val userLists = MutableLiveData<ArrayList<UserFollowData>>()

    fun setFollowList(username: String, type: String) {
        requestApi()
            .getUserFollow(username, type)
            .enqueue(object : Callback<FollowData> {
                override fun onFailure(call: Call<FollowData>, t: Throwable) {
                    Log.d("FollowData", t.message.toString())
                }

                override fun onResponse(call: Call<FollowData>, response: Response<FollowData>) {
                    userLists.postValue(response.body())
                }
            })
    }

    fun getUserList(): LiveData<ArrayList<UserFollowData>> = userLists
}