package com.sampling.test.githubUser.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sampling.test.githubUser.CustomOnItemClickListener
import com.sampling.test.githubUser.DetailUserActivity
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.data.FavoritesData
import com.sampling.test.githubUser.data.UserListData
import com.sampling.test.githubUser.db.Favorite.Companion.CONTENT_URI
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycleview_favorite.view.*
import org.jetbrains.anko.doAsync

class FavoriteAdapter(private val activity: Activity): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFav = ArrayList<FavoritesData>()
        set(listFavs) {
            if (listFavs.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFavs)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(favoritesData: FavoritesData) {
            with(itemView) {
                name_fav.text = favoritesData.name
                company_fav.text = favoritesData.company
                location_fav.text = favoritesData.location

                Picasso.with(itemView.context)
                    .load(favoritesData.avatar)
                    .resize(180, 180)
                    .into(itemView.img_fav)

                detail_fav.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(v: View, position: Int) {
                            val favorite = UserListData(favoritesData.avatar, favoritesData.name)
                            val detailIntent = Intent(activity, DetailUserActivity::class.java)
                            detailIntent.putExtra(DetailUserActivity.EXTRA_DETAIL, favorite)
                            activity.startActivity(detailIntent)
                        }
                    }
                ))
                delete_fav.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(v: View, position: Int) {
                            deleteItem(favoritesData)
                        }
                    }
                ))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFav.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    fun deleteItem(favoritesData: FavoritesData) {
        val dialogTitle = activity.resources.getString(R.string.delete_favorite)
        val dialogMessage = activity.resources.getString(R.string.delete_dialog)
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(true)
            .setPositiveButton("Yes") {_, _ ->
                doAsync {
                    val itemUri = Uri.parse(CONTENT_URI.toString() + "/" + favoritesData.id.toString())
                    activity.contentResolver.delete(itemUri, null, null)
                }
            }
            .setNegativeButton("No") {dialog, _ ->
                dialog?.cancel()
            }
            val dialog = alertDialog.create()
        dialog.show()
    }
}