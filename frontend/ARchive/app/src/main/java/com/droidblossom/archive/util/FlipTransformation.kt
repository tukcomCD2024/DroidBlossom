package com.droidblossom.archive.util

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class FlipTransformation : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("flip_horizontal".toByteArray(StandardCharsets.UTF_8))
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.preScale(-1f, 1f)
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.width, toTransform.height, matrix, true)
    }

    override fun equals(other: Any?): Boolean {
        return other is FlipTransformation
    }

    override fun hashCode(): Int {
        return "flip_horizontal".hashCode()
    }
}