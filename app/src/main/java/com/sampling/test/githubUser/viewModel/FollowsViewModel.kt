package com.sampling.test.githubUser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.sampling.test.githubUser.data.UserFollowData
import okhttp3.OkHttpClient
import org.json.JSONArray
import java.util.concurrent.TimeUnit

class FollowsViewModel: ViewModel() {

    private val userLists = MutableLiveData<ArrayList<UserFollowData>>()

    fun setFollowList(username: String, type: String) {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        AndroidNetworking.get("https://api.github.com/users/{username}/{following}")
            .addHeaders("token","1882d02be7d779f05b966cd7fa363dc9d8e9453d")
            .addPathParameter("following", type)
            .addPathParameter("username", username)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener{
                override fun onResponse(response: JSONArray) {
                    val userDatumData: ArrayList<UserFollowData> = arrayListOf()
                    userDatumData.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val userdata =
                            UserFollowData(
                                avatar = jsonObject.getString("avatar_url"),
                                username = jsonObject.getString("login")
                            )
                        userDatumData.add(userdata)
                    }
                    userLists.postValue(userDatumData)
                }

                override fun onError(anError: ANError?) {
                    Log.d("Error message: ", anError?.message.toString())
                }

            })

    }

    fun getUserList(): LiveData<ArrayList<UserFollowData>> = userLists
}