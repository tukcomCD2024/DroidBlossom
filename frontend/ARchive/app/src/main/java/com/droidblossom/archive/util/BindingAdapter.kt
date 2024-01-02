package com.droidblossom.archive.util

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("bind:imageUrl")
fun ImageView.setImage(imageUrl: String?) {
    Glide.with(this.context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}