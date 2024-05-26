package com.droidblossom.archive.util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.droidblossom.archive.R
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale

@BindingAdapter("textInt")
fun AppCompatTextView.textInt(int: Int) {
    this.text = int.toString()
}

@BindingAdapter(value = ["bind:imageUrl", "bind:placeholder"], requireAll = false)
fun ImageView.setImage(imageUrl: Uri?, placeholder: Drawable?) {
    if (imageUrl == null) {
        placeholder?.let {
            this.setImageDrawable(it)
        }
        return
    }

    placeholder?.let {
        this.setImageDrawable(it)
    }

    val thumbnailRequest = Glide.with(this.context)
        .load(imageUrl)
        .sizeMultiplier(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade(300))

    Glide.with(this.context)
        .load(imageUrl)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .override(this.width, this.height)
        .apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
        }
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "Image load failed")
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "Image loaded successfully")
                this@setImage.setImageDrawable(resource)
                return false
            }
        })
        .into(this)
}

@BindingAdapter(value = ["bind:VideoUri", "bind:placeholder"], requireAll = false)
fun CircleImageView.setThumbUrI(VideoUri: String?, placeholder: Drawable?) {
    if (VideoUri != null) {
        Glide.with(context)
            .load(VideoUri)
            .thumbnail(0.1f)
            .override(this.width, this.height)
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
    if (imageUrl == null) {
        placeholder?.let {
            this.setImageDrawable(it)
        }
        return
    }

    placeholder?.let {
        this.setImageDrawable(it)
    }

    val thumbnailRequest = Glide.with(this.context)
        .load(imageUrl)
        .sizeMultiplier(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
        }

    Glide.with(this.context)
        .load(imageUrl)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .override(this.width, this.height)
        .apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
        }
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "Image load failed")
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "Image loaded successfully")
                this@setImageUrl.setImageDrawable(resource)
                return false
            }
        })
        .into(this)
}

@SuppressLint("CheckResult")
@BindingAdapter(value = ["bind:url", "bind:baseImg"], requireAll = false)
fun ImageView.setUrlImg(imageUrl: String, placeholder: Drawable?) {
    Log.d("이미지", "아래")

    placeholder?.let {
        this.setImageDrawable(it)
    }

    val thumbnailRequest = Glide.with(this.context)
        .load(imageUrl)
        .sizeMultiplier(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                this@setUrlImg.setImageDrawable(resource)
                return false
            }
        })

    // 원본 이미지 로드 요청
    Glide.with(this.context)
        .load(imageUrl)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
        }
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "Image load failed")
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("Glide", "Image loaded successfully")
                this@setUrlImg.setImageDrawable(resource)
                return false
            }
        })
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

@BindingAdapter("bind:culcLastDays")
fun TextView.culcLastDays(date: String) {
    this.text = DateUtils.calcLastDate(date)
}

@BindingAdapter("bind:animateFAB")
fun CardView.animateFAB(y: Float) {
    ObjectAnimator.ofFloat(this, "translationY", y).apply { start() }
}

@BindingAdapter("bind:displayCreationDateTimeFormatted")
fun TextView.setFormattedDateTime(dateString: String) {
    dateString.let {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val date = parser.parse(dateString)

            val formatter = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
            val formattedDate = date?.let { formatter.format(it) }
            this.text = formattedDate
        } catch (e: Exception) {
            this.text = "날짜 형식 오류"
        }
    }
}

@BindingAdapter("bind:displayCreationDateTimeNullFormatted")
fun TextView.setFormattedDateTimeNull(dateString: String?) {
    if (!dateString.isNullOrEmpty()) {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val date = parser.parse(dateString)

            val formatter = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
            val formattedDate = date?.let { formatter.format(it) }
            this.text = formattedDate
        } catch (e: Exception) {
            this.text = "날짜 형식 오류"
        }
    } else {
        this.text = "일반 캡슐 입니다"
    }
}

@BindingAdapter("bind:displayGroupCreationDate")
fun TextView.setGroupCreationDate(dateString: String?) {
    if (!dateString.isNullOrEmpty()) {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val date = parser.parse(dateString)

            val formatter = SimpleDateFormat("yyyy년 MM월 dd일 생성", Locale.getDefault())
            val formattedDate = date?.let { formatter.format(it) }
            this.text = formattedDate
        } catch (e: Exception) {
            this.text = "날짜 형식 오류"
        }
    } else {
        this.text = "일반 캡슐 입니다"
    }
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

@BindingAdapter("bind:minus_plus")
fun ImageView.setMinusPlusImage(flag: Boolean) {
    val resourceId = if (flag) {
        R.drawable.ic_minus_24
    } else {
        R.drawable.ic_plus_main_24
    }
    this.setImageDrawable(ContextCompat.getDrawable(context, resourceId))
}

@BindingAdapter("bind:layout_height_bind")
fun setLayoutHeight(view: View, height: Float) {
    val layoutParams: ViewGroup.LayoutParams = view.layoutParams
    layoutParams.height = height.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("bind:setCapsuleType2Img")
fun ImageView.setCapsuleType2Img(type: String?) {
    when (type) {
        "SECRET" -> {
            this.setImageResource(R.drawable.ic_secret_marker_24)
        }

        "PUBLIC" -> {
            this.setImageResource(R.drawable.ic_public_marker_24)
        }

        "GROUP" -> {
            this.setImageResource(R.drawable.ic_group_marker_24)
        }

        else -> {}
    }
}

@BindingAdapter("bind:setArrowImg")
fun ImageView.setArrowImg(isShowMore: Boolean) {
    if (isShowMore) this.setImageResource(R.drawable.ic_arrow_up_24) else this.setImageResource(R.drawable.ic_arrow_down_24)
}

@BindingAdapter("bind:setTextMaxLines")
fun TextView.setTextMaxLines(isShowMore: Boolean) {
    if (isShowMore) {
        this.maxLines = Int.MAX_VALUE
        this.ellipsize = null
    }else{
        this.maxLines = 1
        this.ellipsize = TextUtils.TruncateAt.END
    }
}


@BindingAdapter("bind:tabMarginEnd")
fun TabLayout.setTabItemMargin(marginEndDp: Int) {
    val marginEndPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, marginEndDp.toFloat(), resources.displayMetrics
    ).toInt()

    val tabs = getChildAt(0) as ViewGroup
    for (i in 0 until tabs.childCount) {
        val tab = tabs.getChildAt(i)
        val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = marginEndPx
        tab.layoutParams = layoutParams
    }
    requestLayout()
}

@BindingAdapter(
    value = ["bind:isFriend", "bind:isInviteToMe", "bind:isInviteToFriend", "bind:friendName"],
    requireAll = false
)
fun TextView.addFriendText(
    isFriend: Boolean,
    isInviteToMe: Boolean,
    isInviteToFriend: Boolean,
    name: String
) {
    if (isFriend || isInviteToFriend || isInviteToMe) {
        if (isFriend) {
            this.text = "이미 친구입니다."
        } else if (isInviteToFriend) {
            this.text = "요청을 보냈습니다."
        } else {
            this.text = "요청을 받았습니다,확인해주세요."
        }
    } else {
        this.text = name
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["bind:count", "bind:showDecimal"], requireAll = true)
fun TextView.formatCountWithK(count: Int, showDecimal: Boolean) {

    if (count < 1000) {
        this.text = count.toString()
    } else {
        if (showDecimal) {
            val thousands = count / 1000
            val remainder = (count % 1000) / 100
            if (remainder == 0) {
                this.text = "${thousands}K"
            } else {
                this.text = "${thousands}.${remainder}K"
            }
        } else {
            this.text = "${count / 1000}K"
        }
    }
}

