package com.example.imageupload

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageAdapter(private var uri:ArrayList<ImageModel>,private var context:Context):RecyclerView.Adapter<ImageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layout=LayoutInflater.from(parent.context).inflate(R.layout.row_image,parent,false)
        
        return ImageHolder(layout)
    }

    override fun getItemCount(): Int {
        return uri.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        Picasso.get().load(uri[position].uri).into(holder.imageView)
    }
}