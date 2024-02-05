package com.droidblossom.archive.presentation.ui.social

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.presentation.ui.skin.SkinFragment

class SocialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social, container, false)
    }


    companion object{

        const val TAG = "Social"
        fun newIntent()= SocialFragment()
    }
}