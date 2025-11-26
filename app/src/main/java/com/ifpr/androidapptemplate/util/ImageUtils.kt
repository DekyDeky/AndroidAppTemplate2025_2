package com.ifpr.androidapptemplate.util

import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadImage(base64: String?, url: String?, imageView: ImageView) {

    when {
        !url.isNullOrEmpty() -> {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        }

        !base64.isNullOrEmpty() -> {
            try {
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                Glide.with(imageView.context)
                    .asBitmap()
                    .load(bytes)
                    .into(imageView)
            } catch (_: Exception) {}
        }
    }
}
