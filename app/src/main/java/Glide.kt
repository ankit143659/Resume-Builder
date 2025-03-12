package com.example.minorproject_resumebuilder.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.minorproject_resumebuilder.R

object GlideHelper {
    fun loadImage(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder)  // While loading
            .error(R.drawable.error_image)       // If failed to load
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Caching
            .into(imageView)
    }
}
