package com.sampling.test.githubUser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sampling.test.githubUser.adapter.SectionsPagerAdapter
import com.sampling.test.githubUser.data.UserListData
import com.sampling.test.githubUser.viewModel.DetailUserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var viewModel: DetailUserViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        (supportActionBar as ActionBar).title = getString(R.string.detail_user)

        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(
            DetailUserViewModel::class.java)
        verifyConnection()

        val user = intent.getParcelableExtra(EXTRA_DETAIL) as UserListData
        username.text = user.username

        //Load image with Picasso
        Picasso.with(this)
            .load(user.avatar)
            .resize(350, 350)
            .into(avatar)

        viewModel.setDetailUser(user.username)
        getDetail()
        getViewPager(user)
    }

    @SuppressLint("SetTextI18n")
    private fun getDetail(){
        //Set detail content by viewModel
        viewModel.getDetailUser().observe(this, Observer { detail ->
            name.text = if(detail.name != "null") detail.name else "-"
            location.text = if(detail.location != "null") detail.location else "-"
            repository.text = "${detail.repository} ${getString(R.string.repos)}"
            company.text = if (detail.company != "null") detail.company else "-"

            progress_bar2.visibility = View.GONE
        })
    }

    private fun getViewPager(user: UserListData){
        //Set view pager
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager,
                user.username
            )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun verifyConnection(){
        viewModel.getConnectionStatus().observe(this, Observer { status ->
            run {
                if (status == "Unavailable") {
                    progress_bar2.visibility = View.GONE
                    Toast.makeText(
                        this,
                        getString(R.string.connection_unavailable),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
