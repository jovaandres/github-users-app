package com.sampling.test.githubUser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.data.UserFollowData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycleview_follow.view.*

class FollowViewAdapter(private val listFollow: ArrayList<UserFollowData>) :
    RecyclerView.Adapter<FollowViewAdapter.FollowViewHolder>() {

    inner class FollowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycleview_follow, parent, false)
        return FollowViewHolder(view)
    }

    override fun getItemCount(): Int = listFollow.size

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val follow = listFollow[position]

        Picasso.get()
            .load(follow.avatar_url)
            .resize(125, 125)
            .into(holder.itemView.follow_avatar)

        holder.itemView.follow_name.text = follow.login
    }
}