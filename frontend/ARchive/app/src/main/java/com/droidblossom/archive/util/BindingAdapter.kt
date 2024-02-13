package com.droidblossom.archive.util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale

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

@BindingAdapter(value = ["bind:VideoUri", "bind:placeholder"], requireAll = false)
fun CircleImageView.setThumbUrI(VideoUri: String?, placeholder: Drawable?) {
    if (VideoUri != null) {
        Glide.with(context)
            .load(VideoUri)
            .thumbnail(0.1f)
            .apply {
                if (placeholder != null) {
                    placeholder(placeholder)
                }
            }
            .into(this)
    }
}


@BindingAdapter(value = ["bind:imageUrl", "bind:placeholder"], requireAll = false)
fun CircleImageView.setImageUrl(imageUrl: String?, placeholder: Drawable?) {
    if (imageUrl != null) {
        Glide.with(context)
            .load(imageUrl)
            .apply {
                if (placeholder != null) {
                    placeholder(placeholder)
                }
            }
            .into(this)
    }
}

@SuppressLint("CheckResult")
@BindingAdapter(value = ["bind:url", "bind:baseImg"], requireAll = false)
fun ImageView.setUrlImg(imageUrl: String, placeholder: Drawable?) {
    Glide.with(this.context)
        .load(imageUrl)
        .placeholder(placeholder)
        .error(placeholder)
        .apply(RequestOptions().fitCenter())
        .into(this)
}

@BindingAdapter("bind:applyPhoneNumberFormatting")
fun EditText.applyPhoneNumberFormatting(apply: Boolean) {
    // 나중에는 그냥 지역번호를 받아서 각각의 포맷이 되도록 하는게 맞음
    if (apply) {
        addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }
}

@BindingAdapter("bind:displayRemainingTime")
fun TextView.displayRemainingTime(totalSeconds: Int) {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    this.text = String.format("%02d분 %02d초", minutes, seconds)
}

@BindingAdapter("bind:animateFAB")
fun CardView.animateFAB(y : Float){
    ObjectAnimator.ofFloat(this, "translationY", y).apply { start() }
}

@BindingAdapter("bind:displayCreationDateFormatted")
fun TextView.setFormattedDate(dateString: String) {
    dateString.let {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val date = parser.parse(dateString)

            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val formattedDate = date?.let { formatter.format(it) }
            this.text = formattedDate
        } catch (e: Exception) {
            this.text = "날짜 형식 오류"
        }
    }
}

@BindingAdapter("bind:srcCompat")
fun ImageView.setImageResource(resource: Int?) {
    resource?.let {
        this.setImageResource(it)
    }
}

@BindingAdapter("bind:layout_height_bind")
fun setLayoutHeight(view: View, height: Float) {
    val layoutParams: ViewGroup.LayoutParams = view.layoutParams
    layoutParams.height = height.toInt()
    view.layoutParams = layoutParams
}