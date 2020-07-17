package com.sampling.test.githubUser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.sampling.test.githubUser.data.UserListData
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SearchListViewModel : ViewModel() {

    val userLists = MutableLiveData<ArrayList<UserListData>>()
    val connectionStatus = MutableLiveData<String>()

    //Request data from api and put in userdetaildata
    fun setSearchList(username: String) {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        AndroidNetworking.get("https://api.github.com/search/users")//add api token
            .addQueryParameter("q", username)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject) {
                    connectionStatus.postValue("Available")
                    val jsonArray = response.getJSONArray("items")
                    val userListData: ArrayList<UserListData> = arrayListOf()
                    userListData.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userdata =
                            UserListData(
                                avatar = jsonObject.getString("avatar_url"),
                                username = jsonObject.getString("login")
                            )
                        userListData.add(userdata)
                    }
                    userLists.postValue(userListData)
                }

                override fun onError(anError: ANError?) {
                    Log.d("onError", anError?.message.toString())
                    connectionStatus.postValue("Unavailable")
                }
            })
    }

    //get putted data
    fun getSearchList(): LiveData<ArrayList<UserListData>> = userLists
    fun getConnectionStatus(): LiveData<String> = connectionStatus
}