package com.sampling.test.githubUser.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sampling.test.githubUser.CustomOnItemClickListener
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.data.UserListData
import com.sampling.test.githubUser.ui.DetailUserActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycleview_github_user.view.*

class CardViewAdapter(
    private val listUserData: ArrayList<UserListData>,
    private val activity: Activity
) : RecyclerView.Adapter<CardViewAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(listData: UserListData) {
            with(itemView) {
                tv_username.text = listData.login
                Picasso.get()
                    .load(listData.avatar_url)
                    .resize(135, 135)
                    .into(itemView.img_avatar)

                detail_button.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(v: View, position: Int) {
                            showDetail(listData)
                        }
                    }
                ))
            }
        }
    }

    //Set layout, view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycleview_github_user, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listUserData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listUserData[position])
    }

    //Show detail by Parcelable Intent
    private fun showDetail(listData: UserListData) {
        val user = UserListData(
            listData.avatar_url,
            listData.login
        )
        val detailIntent = Intent(activity, DetailUserActivity::class.java)
        detailIntent.action = "FROM MAIN ACTIVITY"
        detailIntent.putExtra(DetailUserActivity.EXTRA_DETAIL, user)
        activity.startActivity(detailIntent)
    }
}