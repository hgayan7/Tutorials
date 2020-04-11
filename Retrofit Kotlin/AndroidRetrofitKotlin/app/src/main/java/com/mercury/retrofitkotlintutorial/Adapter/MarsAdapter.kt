package com.mercury.retrofitkotlintutorial.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mercury.retrofitkotlintutorial.Model.Photo
import com.mercury.retrofitkotlintutorial.R
import kotlinx.android.synthetic.main.photo_cell.view.*

class MarsAdapter(private val photos : List<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.photo_cell, parent, false)
        return PhotoHolder(v)}

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PhotoHolder ->{
                holder.bind(photos[position])
            }
        }
    }

    class PhotoHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(photo : Photo){
            val image = "${photo.img_src}".replace("http","https")
            Glide.with(itemView.context).load(image).into(itemView.roverImage)
            itemView.roverName.text = photo.rover.name
            itemView.earthDate.text = photo.earth_date
            itemView.cameraName.text = photo.camera.name
        }
    }
}