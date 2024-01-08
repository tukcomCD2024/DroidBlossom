package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityCreateCapsuleBinding

class CreateCapsuleActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateCapsuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCapsuleBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_create_capsule)

    }
    companion object {
        const val CREATE_CAPSULE = "create_capsule"

        fun newIntent(context: Context) =
            Intent(context, CreateCapsuleActivity::class.java)
    }
}