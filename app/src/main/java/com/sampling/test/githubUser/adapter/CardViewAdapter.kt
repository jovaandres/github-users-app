package com.sampling.test.githubUser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.data.UserListData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycleview_github_user.view.*

class CardViewAdapter(private val listUserData: ArrayList<UserListData>): RecyclerView.Adapter<CardViewAdapter.CardViewHolder>() {

    //Make on item list listener function that use to move to detail activity when it clicked
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    //Set layout, view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview_github_user, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listUserData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val user = listUserData[position]

        //Load image with Picasso
        Picasso.with(holder.itemView.context)
            .load(user.avatar)
            .resize(135, 135)
            .into(holder.itemView.img_avatar)

        //Load data to list by ViewHolder
        holder.itemView.apply {
            tv_username.text = user.username
            detail_button
            detail_button.setOnClickListener {onItemClickCallback.onItemClicked(listUserData[holder.adapterPosition])}
        }
    }

    //OnItemClickCallback interface so main activity can implement the method
    interface OnItemClickCallback{
        fun onItemClicked(listData: UserListData)
    }
}