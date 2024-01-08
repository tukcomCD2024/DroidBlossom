package com.droidblossom.archive.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityMainBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<Nothing?, ActivityMainBinding>(R.layout.activity_main) {

    override val viewModel: Nothing? = null

    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val navController = findNavController(R.id.mainNavHost)
        NavigationUI.setupWithNavController(binding.bottomNavigation, findNavController(R.id.mainNavHost))

        binding.fab.setOnClickListener {
            navController.navigate(
                R.id.cameraFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.nav_main_graph, true)
                    .build())
            binding.bottomNavigation.selectedItemId = R.id.menuCamera
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id != item.itemId) {
                when (item.itemId) {
                    R.id.menuHome -> {
                        navController.navigate(
                            R.id.homeFragment, null, NavOptions.Builder()
                                .setPopUpTo(R.id.nav_main_graph, true)
                                .build())
                        true
                    }
                    R.id.menuSkin -> {
                        navController.navigate(
                            R.id.skinFragment, null, NavOptions.Builder()
                                .setPopUpTo(R.id.nav_main_graph, true)
                                .build())
                        true
                    }
                    R.id.menuMyPage -> {
                        navController.navigate(
                            R.id.myPageFragment, null, NavOptions.Builder()
                                .setPopUpTo(R.id.nav_main_graph, true)
                                .build())
                        true
                    }
                    R.id.menuSocial -> {
                        navController.navigate(
                            R.id.socialFragment, null, NavOptions.Builder()
                                .setPopUpTo(R.id.nav_main_graph, true)
                                .build())
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }

    }

    companion object{
        fun goMain(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

}