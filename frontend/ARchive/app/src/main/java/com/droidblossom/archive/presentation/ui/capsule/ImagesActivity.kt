package com.droidblossom.archive.presentation.ui.capsule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.viewpager2.widget.ViewPager2
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityImagesBinding
import com.droidblossom.archive.databinding.ActivityVideoBinding
import com.droidblossom.archive.domain.model.common.ImageUrl
import com.droidblossom.archive.presentation.ui.capsule.VideoActivity.Companion.VIDEO
import com.droidblossom.archive.presentation.ui.capsule.adapter.ImageDetailRVA
import com.droidblossom.archive.presentation.ui.capsule.adapter.ImageUrlRVA
import com.droidblossom.archive.util.getStatusBarHeight

class ImagesActivity : AppCompatActivity() {
    lateinit var binding: ActivityImagesBinding

    private val imageVP by lazy {
        ImageDetailRVA()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initVP()
    }

    private fun initView(){

    }

    private fun initVP(){
        binding.vp.adapter = imageVP

        val images = (intent.getStringArrayExtra(IMAGES) ?: emptyArray()).map { uriString ->
            ImageUrl(uriString)
        }
        imageVP.submitList(images)
        binding.totalT.text = images.size.toString()
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.currentT.text = "${intent.getIntExtra(CURRENT,1)+1}/"
        binding.vp.currentItem = intent.getIntExtra(CURRENT,0)
        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                binding.currentT.text = "${position + 1}/"
            }
        })
    }


    companion object {
        const val IMAGES = "images"
        const val CURRENT = "current"

        fun newIntent(context: Context, imageUrls : Array<String> ,current : Int) =
            Intent(context, ImagesActivity::class.java).apply {
                putExtra(IMAGES, imageUrls)
                putExtra(CURRENT, current)
            }
    }
}