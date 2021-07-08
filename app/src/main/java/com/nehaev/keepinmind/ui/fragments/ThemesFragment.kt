package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.ThemesAdapter
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.ui.viewmodels.MindViewModel
import com.nehaev.keepinmind.ui.viewmodels.ThemeViewModel
import com.nehaev.keepinmind.util.ThemeResource
import kotlinx.android.synthetic.main.fragment_themes.*

class ThemesFragment : Fragment(R.layout.fragment_themes) {

    private val TAG = "ThemesFragment"

    lateinit var viewModel: ThemeViewModel
    lateinit var themesAdapter: ThemesAdapter

    private var nameCounter = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MindActivity).viewModel.themeViewModel
        viewModel.attach()

        setupRecyclerView()
        setFabAction()
        setObserver()
    }

    override fun onStop() {
        super.onStop()
        viewModel.detach()
    }

    private fun setupRecyclerView() {
        themesAdapter = ThemesAdapter()
        rvThemes.apply {
            adapter = themesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is ThemeResource.Success -> {
                    themesAdapter.differ.submitList(response.itemList)
                    hideProgressbar()
                }
                is ThemeResource.Loading -> {
                    showProgressbar()
                }
                is ThemeResource.Error -> {
                    showErrorMessage(response.message)
                }
            }
        })
    }

    private fun successScreenState() {
        tvThemeErrorMessage.visibility = View.GONE
        pbThemesLoading.visibility = View.GONE
        rvThemes.visibility = View.VISIBLE
    }

    private fun showErrorMessage(message: String?) {
        pbThemesLoading.visibility = View.GONE
        rvThemes.visibility = View.GONE
        tvThemeErrorMessage.visibility = View.VISIBLE

        tvThemeErrorMessage.text = message ?: "Error"
    }

    private fun hideProgressbar() {
        pbThemesLoading.visibility = View.GONE
        tvThemeErrorMessage.visibility = View.GONE
        rvThemes.visibility = View.VISIBLE
    }

    private fun showProgressbar() {
        pbThemesLoading.visibility = View.VISIBLE
        rvThemes.visibility = View.GONE
        tvThemeErrorMessage.visibility = View.GONE
    }

    private fun setFabAction() {
        fabAddTheme.setOnClickListener {

            for ( i in 0..3) {
                nameCounter++
                viewModel.upsertTheme(Theme(
                    id = nameCounter,
                    categoryId = nameCounter,
                    categoryName = "Android",
                    name = "Theme $nameCounter",
                    questionCnt = 5 + nameCounter
                ))
            }
            for ( i in 0..3) {
                nameCounter++
                viewModel.upsertTheme(Theme(
                    id = nameCounter,
                    categoryId = nameCounter,
                    categoryName = "IOS",
                    name = "Theme $nameCounter",
                    questionCnt = 5 + nameCounter
                ))
            }

            Snackbar.make(rvThemes, "Added!", Snackbar.LENGTH_SHORT).show()
        }
    }
}





























