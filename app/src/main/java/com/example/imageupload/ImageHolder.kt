package com.example.imageupload

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageHolder(view:View):RecyclerView.ViewHolder(view) {

    val imageView:ImageView=view.findViewById(R.id.imageView)
}