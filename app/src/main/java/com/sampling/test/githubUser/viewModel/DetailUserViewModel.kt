package com.sampling.test.githubUser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.sampling.test.githubUser.data.UserDetailData
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class DetailUserViewModel : ViewModel() {

    val userDetail = MutableLiveData<UserDetailData>()
    val connectionStatus = MutableLiveData<String>()

    //Request data from api and put in userdetaildata
    fun setDetailUser(username: String) {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        AndroidNetworking.get("https://api.github.com/users/{username}")//add api token
            .addPathParameter("username", username)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject) {
                    connectionStatus.postValue("Available")
                    val userdata = UserDetailData(
                        name = response.getString("name"),
                        company = response.getString("company"),
                        location = response.getString("location"),
                        public_repos = response.getInt("public_repos")
                    )
                    userDetail.postValue(userdata)
                }

                override fun onError(anError: ANError?) {
                    connectionStatus.postValue("Unavailable")
                    Log.d("Error message", anError?.message.toString())
                }
            })
    }

    //get putted data
    fun getDetailUser(): LiveData<UserDetailData> = userDetail
    fun getConnectionStatus(): LiveData<String> = connectionStatus
}