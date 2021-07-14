package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.ui.viewmodels.CategoryChoiceViewModel
import com.nehaev.keepinmind.util.Resource

class ThemeCreateFragment : Fragment(R.layout.fragmentdialog_choice_categoty) {

    private val TAG = "ThemeCreateFragment"

    private lateinit var viewModel: CategoryChoiceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MindActivity).viewModel.categoryChoiceViewModel
        viewModel.attach()

        setObserver()
        setupButtonSave()
    }

    private fun setupButtonSave() {

    }

    private fun setObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    //setSpinnerData(response.data ?: listOf<String>())
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }
        })
    }

    private fun setSpinnerData(categories: List<String>) {
//        spinnerCategories.adapter = ArrayAdapter(
//            activity as MindActivity,
//            R.layout.support_simple_spinner_dropdown_item,
//            categories
//        )
    }

    override fun onStop() {
        super.onStop()
        viewModel.detach()
    }


}