package com.sampling.test.githubUser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.adapter.FollowViewAdapter
import com.sampling.test.githubUser.data.UserFollowData
import com.sampling.test.githubUser.viewModel.FollowsViewModel
import kotlinx.android.synthetic.main.follow_fragment.*

class FollowFragment : Fragment() {

    companion object {
        fun newInstance(username: String, index: Int) =
            FollowFragment().apply {
                val type = when(index){
                    0 -> "followers"
                    1 -> "following"
                    else -> "null"
                }
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                    putString(TYPE, type)
                }
            }
        private const val TYPE = "type"
        private const val USERNAME = "username"
    }

    private lateinit var viewModel: FollowsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.follow_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowsViewModel::class.java)
        // TODO: Use the ViewModel
        val username = arguments?.getString(USERNAME).toString()
        val type = arguments?.getString(TYPE).toString()

        viewModel.apply {
            setFollowList(username, type)
        }
        viewModel.getUserList().observe(viewLifecycleOwner, Observer { lists ->
            run{
                viewModeling(lists)
            }
        })
    }

    //make followers/following list
    private fun viewModeling(lists: ArrayList<UserFollowData>){
        rv_follow.setHasFixedSize(true)
        rv_follow.layoutManager = LinearLayoutManager(activity)
        val followViewAdapter =
            FollowViewAdapter(lists)
        rv_follow.adapter = followViewAdapter
    }

}