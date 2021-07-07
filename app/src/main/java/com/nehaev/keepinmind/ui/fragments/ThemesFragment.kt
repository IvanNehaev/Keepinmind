package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.ui.viewmodels.MindViewModel
import com.nehaev.keepinmind.ui.viewmodels.ThemeViewModel
import com.nehaev.keepinmind.util.ThemeResource

class ThemesFragment : Fragment(R.layout.fragment_themes) {

    private val TAG = "ThemesFragment"

    lateinit var viewModel: ThemeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MindActivity).viewModel.themeViewModel
        viewModel.attach()

        viewModel.liveData.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is ThemeResource.Success -> {

                }
                is ThemeResource.Loading -> {

                }
                is ThemeResource.Error -> {

                }
            }
        })
    }


    override fun onStop() {
        super.onStop()

        viewModel.detach()
    }


}