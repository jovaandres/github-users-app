package com.sampling.test.githubUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sampling.test.githubUser.adapter.CardViewAdapter
import com.sampling.test.githubUser.data.UserListData
import com.sampling.test.githubUser.viewModel.SearchListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SearchListViewModel::class.java)
        getUserList()
        search()
        verifyConnection()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.languange_setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if( item.itemId == R.id.language_change_setting) {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun verifyConnection(){
        viewModel.getConnectionStatus().observe(this, Observer { status ->
            run {
                if (status == "Unavailable") {
                    progress_bar.hide()
                    Toast.makeText(
                        this,
                        getString(R.string.connection_unavailable),
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun search() {
        search as SearchView
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setSearchList(query)
                progress_bar.show()
                val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getUserList(){
        rv_user.layoutManager = LinearLayoutManager(this)
        viewModel.getSearchList().observe(this, Observer { list ->
            run {
                rv_user.setHasFixedSize(true)
                val cardViewAdapter =
                    CardViewAdapter(list)
                rv_user.adapter = cardViewAdapter
                progress_bar.hide()
                if(list.size == 0) Toast.makeText(this@MainActivity,
                    getString(R.string.not_found), Toast.LENGTH_SHORT).show()

                cardViewAdapter.setOnItemClickCallback(object : CardViewAdapter.OnItemClickCallback {
                    override fun onItemClicked(listData: UserListData) {
                        showDetail(listData)
                    }
                })
            }
        })
    }

    //Show detail by Parcelable Intent
    private fun showDetail(listData: UserListData){
        val user = UserListData(
            listData.avatar,
            listData.username
        )
        val detailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        detailIntent.putExtra(DetailUserActivity.EXTRA_DETAIL, user)
        startActivity(detailIntent)
    }
}


