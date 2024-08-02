package com.droidblossom.archive.presentation.ui.permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityPermissionBinding
import com.droidblossom.archive.presentation.ui.auth.AuthActivity
import com.droidblossom.archive.util.PermissionsUtil

class PermissionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPermissionBinding
    private lateinit var permissionsUtil: PermissionsUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionsUtil = PermissionsUtil(this)
        initView()

        if (permissionsUtil.areEssentialPermissionsGranted()){
            //AuthActivity.goAuth(this)
        }
    }

    private fun initView(){

        with(binding){

            confirmBtn.setOnClickListener {
                permissionsUtil.requestAllPermissions()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (permissionsUtil.areEssentialPermissionsGranted()) {

        } else {


        }
    }
}