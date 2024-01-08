package com.droidblossom.archive.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter(value = ["bind:imageUrl", "bind:placeholder"], requireAll = false)
fun ImageView.setImage(imageUrl: Uri?, placeholder: Drawable?) {
    Glide.with(this.context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
        }
        .into(this)
}
