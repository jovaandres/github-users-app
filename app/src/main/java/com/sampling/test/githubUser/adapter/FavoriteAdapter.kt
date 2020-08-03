package com.sampling.test.githubUser.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sampling.test.githubUser.CustomOnItemClickListener
import com.sampling.test.githubUser.R
import com.sampling.test.githubUser.data.UserListData
import com.sampling.test.githubUser.db.Favorite
import com.sampling.test.githubUser.db.Favorite.Companion.CONTENT_URI
import com.sampling.test.githubUser.helper.MappingHelper.toBitmap
import com.sampling.test.githubUser.ui.DetailUserActivity
import com.sampling.test.githubUser.widget.FavoriteUserWidget
import kotlinx.android.synthetic.main.item_recycleview_favorite.view.*
import org.jetbrains.anko.doAsync

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFav = ArrayList<Favorite>()
        set(listFavs) {
            if (listFavs.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFavs)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favoritesData: Favorite) {
            with(itemView) {
                name_fav.text = favoritesData.name
                company_fav.text = favoritesData.company
                location_fav.text = favoritesData.location
                img_fav.setImageBitmap(toBitmap(favoritesData.avatar))

                detail_fav.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(v: View, position: Int) {
                            val favorite = UserListData(login = favoritesData.name, offlineAvatar = favoritesData.avatar)
                            val detailIntent = Intent(activity, DetailUserActivity::class.java)
                            detailIntent.putExtra(DetailUserActivity.EXTRA_DETAIL, favorite)
                            detailIntent.action = "FROM FAVORITE ACTIVITY"
                            activity.startActivity(detailIntent)
                        }
                    }
                ))
                delete_fav.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(v: View, position: Int) {
                            deleteItem(context, favoritesData)
                        }
                    }
                ))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycleview_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFav.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    //delete item and notify data changed in widget
    fun deleteItem(context: Context, favoritesData: Favorite) {
        val dialogTitle = activity.resources.getString(R.string.delete_favorite)
        val dialogMessage = activity.resources.getString(R.string.delete_dialog)
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                doAsync {
                    val itemUri =
                        Uri.parse(CONTENT_URI.toString() + "/" + favoritesData.id.toString())
                    activity.contentResolver.delete(itemUri, null, null)

                    val intent = Intent(context, FavoriteUserWidget::class.java)
                    intent.action = FavoriteUserWidget.UPDATE_ITEM
                    context.sendBroadcast(intent)
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog?.cancel()
            }
        val dialog = alertDialog.create()
        dialog.show()
    }
}