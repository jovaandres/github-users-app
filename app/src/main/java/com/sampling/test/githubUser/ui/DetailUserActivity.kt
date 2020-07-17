package com.sampling.test.githubUser.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.adapter.SectionsPagerAdapter
import com.sampling.test.githubUser.data.UserListData
import com.sampling.test.githubUser.db.Favorite
import com.sampling.test.githubUser.db.Favorite.Companion.CONTENT_URI
import com.sampling.test.githubUser.viewModel.DetailUserViewModel
import com.sampling.test.githubUser.widget.FavoriteUserWidget
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


        val user = intent.getParcelableExtra(EXTRA_DETAIL) as UserListData
        username.text = user.username

        //Load image with Picasso
        Picasso.get()
            .load(user.avatar)
            .resize(300, 300)
            .into(avatar)

        viewModel.setDetailUser(user.username)
        getDetail()
        getViewPager(user)

        favoriteCheck(user.username)

        fab_fav.setOnClickListener {
            val values = ContentValues()
            doAsync {
                values.apply {
                    put(Favorite.COLUMN_FAVORITE_NAME, user.username)
                    put(Favorite.COLUMN_FAVORITE_AVATAR, user.avatar)
                    put(Favorite.COLUMN_FAVORITE_COMPANY, company.text.toString())
                    put(Favorite.COLUMN_FAVORITE_LOCATION, location.text.toString())
                }
                sendUpdate(applicationContext)
                uiThread {
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(
                        this@DetailUserActivity,
                        resources.getString(R.string.add_favorite, user.username),
                        Toast.LENGTH_SHORT
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
        viewModel.getDetailUser().observe(this, Observer { detail ->
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
                user.username
            )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun verifyConnection() {
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
