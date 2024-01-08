package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivitySkinMakeBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkinMakeActivity : BaseActivity<SkinMakeViewModelImpl, ActivitySkinMakeBinding>(R.layout.activity_skin_make) {

    override val viewModel: SkinMakeViewModelImpl by viewModels()

    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeData()
    }

    companion object{
        fun goSkinMake(context: Context) {
            val intent = Intent(context, SkinMakeActivity::class.java)
            context.startActivity(intent)
        }
    }
}