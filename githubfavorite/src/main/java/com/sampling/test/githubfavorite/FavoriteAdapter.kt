package com.sampling.test.githubfavorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sampling.test.githubfavorite.MappingHelper.toBitmap
import kotlinx.android.synthetic.main.item_recycleview_favorite.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

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
                img_fav.setImageBitmap(toBitmap(favoritesData.avatar))
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
}