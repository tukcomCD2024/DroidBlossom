package com.droidblossom.archive.util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.TextUtils
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.droidblossom.archive.R
import com.droidblossom.archive.presentation.ui.home.HomeFragment
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

    Glide.with(this.context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .override(this.width, this.height)
        .apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
        }
        .into(this)
}

@SuppressLint("CheckResult")
@BindingAdapter(value = ["capsulePreviewImageUrl", "capsuleType", "userPlaceholder", "appPlaceholder"], requireAll = false)
fun CircleImageView.capsulePreviewSetImage(
    imageUrl: String?,
    capsuleType: HomeFragment.CapsuleType?,
    userPlaceholder: Drawable?,
    appPlaceholder: Drawable?
) {

    Glide.with(this.context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .override(this.width, this.height)
        .apply {
            when (capsuleType) {
                HomeFragment.CapsuleType.TREASURE -> placeholder(appPlaceholder)
                else -> placeholder(userPlaceholder)
            }
        }
        .into(this)
}

@BindingAdapter(value = ["bind:VideoUri", "bind:placeholder"], requireAll = false)
fun CircleImageView.setThumbUrI(VideoUri: String?, placeholder: Drawable?) {
    if (VideoUri == null) {
        this.setImageDrawable(placeholder)
        return
    }

    Glide.with(context)
        .load(VideoUri)
        .override(this.width, this.height)
        .thumbnail(0.1f)
        .placeholder(placeholder)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}

@BindingAdapter(value = ["bind:imageUrl", "bind:placeholder"], requireAll = false)
fun CircleImageView.setImageUrl(imageUrl: String?, placeholder: Drawable?) {
    if (imageUrl != null) {
        Glide.with(context)
            .load(imageUrl)
            .override(this.width, this.height)
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
        .override(this.width, this.height)
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
        this.text = ""
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
            this.text = "보물 캡슐"
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

@BindingAdapter("bind:checkBox")
fun ImageView.setCheckBox(state: Boolean) {
    if (state) this.setImageResource(R.drawable.ic_check_box_24)
    else this.setImageResource(R.drawable.ic_check_box_blank_24)
}

@BindingAdapter("bind:socialType")
fun ImageView.setSocialType(type: String?) {
    when (type) {
        "KAKAO" -> {
            this.setImageResource(R.drawable.ic_kakao_logo)
        }

        "GOOGLE" -> {
            this.setImageResource(R.drawable.ic_google_logo)
        }

        else -> {}
    }
}

@BindingAdapter("bind:numberBlind")
fun TextView.setSocialType(phoneNum: String?) {
    phoneNum?.let {

        if (phoneNum.length <6){
            this.text = ""
        } else if (phoneNum.length in 6..10) {
            val firstPart = phoneNum.substring(0, 5)
            this.text = "${firstPart}XXXX"
        } else {
            val firstPart = phoneNum.substring(0, 5)
            val lastPart = phoneNum.substring(9)
            this.text = "${firstPart}XXXX$lastPart"
        }
    } ?:run {
        this.text = ""
    }
}

@BindingAdapter("bind:setCapsuleTypeColor")
fun View.setCapsuleTypeColor(type: String?) {
    when (type) {
        "SECRET" -> {
            this.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.purple))
        }

        "PUBLIC" -> {
            this.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.main_1))
        }

        "GROUP" -> {
            this.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.main_2))
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
    } else {
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

