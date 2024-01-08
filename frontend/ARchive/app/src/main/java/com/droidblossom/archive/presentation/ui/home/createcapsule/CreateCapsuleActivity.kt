package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityCreateCapsuleBinding
import com.droidblossom.archive.presentation.base.BaseActivity

class CreateCapsuleActivity : BaseActivity<CreateCapsuleViewModelImpl, ActivityCreateCapsuleBinding>(R.layout.activity_create_capsule) {

    override val viewModel: CreateCapsuleViewModelImpl?  by viewModels()

    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    companion object {
        const val CREATE_CAPSULE = "create_capsule"

        fun newIntent(context: Context) =
            Intent(context, CreateCapsuleActivity::class.java)
    }
}