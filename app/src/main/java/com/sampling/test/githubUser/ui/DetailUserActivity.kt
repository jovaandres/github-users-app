package com.sampling.test.githubUser.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sampling.test.githubUser.Other.NO_NETWORK
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.adapter.SectionsPagerAdapter
import com.sampling.test.githubUser.data.UserListData
import com.sampling.test.githubUser.db.Favorite
import com.sampling.test.githubUser.db.Favorite.Companion.CONTENT_URI
import com.sampling.test.githubUser.helper.MappingHelper.fromBitmap
import com.sampling.test.githubUser.helper.MappingHelper.toBitmap
import com.sampling.test.githubUser.viewModel.DetailUserViewModel
import com.sampling.test.githubUser.widget.FavoriteUserWidget
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_user.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var viewModel: DetailUserViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        (supportActionBar as ActionBar).title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailUserViewModel::class.java
        )
        verifyConnection()


        val user: UserListData = intent.getParcelableExtra(EXTRA_DETAIL)!!
        username.text = user.login

        when (intent.action) {
            "FROM MAIN ACTIVITY" -> {
                //Load image with Picasso
                Picasso.get()
                    .load(user.avatar_url)
                    .resize(300, 300)
                    .into(avatar)
            }
            "FROM FAVORITE ACTIVITY" -> {
                avatar.setImageBitmap(user.offlineAvatar?.let { toBitmap(it) })
            }

        }


        viewModel.setDetailUser(user.login)
        getDetail()
        getViewPager(user)

        favoriteCheck(user.login)

        fab_fav.setOnClickListener {
            val values = ContentValues()
            doAsync {
                values.apply {
                    put(Favorite.COLUMN_FAVORITE_NAME, user.login)
                    put(Favorite.COLUMN_FAVORITE_AVATAR,
                        user.avatar_url?.let { fromBitmap(it) })
                    put(Favorite.COLUMN_FAVORITE_COMPANY, company.text.toString())
                    put(Favorite.COLUMN_FAVORITE_LOCATION, location.text.toString())
                }
                sendUpdate(applicationContext)
                uiThread {
                    contentResolver.insert(CONTENT_URI, values)
                    FancyToast.makeText(
                        this@DetailUserActivity,
                        resources.getString(R.string.add_favorite, user.login),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        true
                    ).show()
                    fab_fav.apply {
                        setImageResource(R.drawable.ic_baseline_favorite_24)
                        isClickable = false
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun getDetail() {
        //Set detail content by viewModel
        viewModel.getDetailUser().observe(this,  { detail ->
            name.text = if (detail.name != "null") detail.name else "-"
            location.text = if (detail.location != "null") detail.location else "-"
            repository.text = "${detail.public_repos} ${getString(R.string.repos)}"
            company.text = if (detail.company != "null") detail.company else "-"

            progress_bar2.visibility = View.GONE
        })
    }

    private fun getViewPager(user: UserListData) {
        //Set view pager
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager,
                user.login
            )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun verifyConnection() {
        viewModel.getConnectionStatus().observe(this,  { status ->
            run {
                if (status == NO_NETWORK) {
                    progress_bar2.visibility = View.GONE
                    FancyToast.makeText(
                        this,
                        getString(R.string.connection_unavailable),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        true
                    ).show()
                }
            }
        })
    }

    //favorite check to avoid double data
    private fun favoriteCheck(user: String) {
        doAsync {
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            cursor?.apply {
                while (moveToNext()) {
                    val name = getString(getColumnIndex(Favorite.COLUMN_FAVORITE_NAME)).toString()
                    if (name == user) {
                        fab_fav.apply {
                            setImageResource(R.drawable.ic_baseline_favorite_24)
                            isClickable = false
                        }
                        break
                    }
                }
                cursor.close()
            }
        }
    }

    //notify data changed in widget
    private fun sendUpdate(context: Context) {
        val intent = Intent(context, FavoriteUserWidget::class.java)
        intent.action = FavoriteUserWidget.UPDATE_ITEM
        context.sendBroadcast(intent)
    }
}
