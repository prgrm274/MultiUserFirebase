package com.programmer270487.loginsignuptypicode.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.programmer270487.loginsignuptypicode.data.model.Photo
import com.programmer270487.loginsignuptypicode.databinding.ItemHomeBinding
import com.squareup.picasso.Picasso

class HomeAdapter(private val photos: MutableList<Photo>) : RecyclerView.Adapter<HomeAdapter.PhotoViewHolder>() {
    fun add(newList: List<Photo>) {
        val start = photos.size
        photos.addAll(newList)
        notifyItemRangeInserted(start, newList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val b = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(b)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo) // Bind photo data to the view holder
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class PhotoViewHolder(private val b: ItemHomeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(photo: Photo) {
            b.apply {
                photoId.text = photo.id.toString()
                photoTitle.text = photo.title
                photoUrl.text = photo.url // as an url
                Picasso.get()
                    .load(photo.thumbnailUrl)
                    .into(photoThumbnail)
            }
        }
    }
}
