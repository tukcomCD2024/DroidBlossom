package com.droidblossom.archive.presentation.ui.skin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinBinding
import com.droidblossom.archive.presentation.ui.skin.skinmake.SkinMakeActivity

class SkinFragment : Fragment() {

    private var _binding : FragmentSkinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSkinBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            SkinMakeActivity.goSkinMake(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}