package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.CategoriesAdapter
import com.nehaev.keepinmind.ui.viewmodels.ThemeCreateViewModel
import com.nehaev.keepinmind.util.Resource
import kotlinx.android.synthetic.main.fragment_create_theme.*

class ThemeCreateDialog : DialogFragment() {

    private val TAG = "ThemeCreateDialog"

    private lateinit var viewModel: ThemeCreateViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupDialog()
        return inflater.inflate(R.layout.fragment_create_theme, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MindActivity).viewModel.themeCreateViewModel
        viewModel.attach()

        setupRecyclerView()
        setObserver()
    }

    private fun setupDialog() {
        val window = dialog?.window
        // set bottom gravity
        window?.setGravity(Gravity.BOTTOM or Gravity.CENTER)
        // set width to MATCH PARENT
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(width, height)
    }

    override fun onStop() {
        super.onStop()
        viewModel.detach()
    }

    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        rvChoiceCategory.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    viewSuccessState()
                    categoriesAdapter.differ.submitList(response.data)
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    viewErrorState()
                }
            }
        })
    }

    private fun viewSuccessState() {
        tvEmptyCategoriesList.visibility = View.GONE
        rvChoiceCategory.visibility = View.VISIBLE
    }

    private fun viewErrorState() {
        tvEmptyCategoriesList.visibility = View.VISIBLE
        rvChoiceCategory.visibility = View.INVISIBLE
    }
}