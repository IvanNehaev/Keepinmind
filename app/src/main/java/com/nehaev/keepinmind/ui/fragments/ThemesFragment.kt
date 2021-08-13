package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.ThemesAdapter
import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.ui.viewmodels.ThemeViewModel
import com.nehaev.keepinmind.util.DialogClickListener
import com.nehaev.keepinmind.util.ThemeResource
import kotlinx.android.synthetic.main.fragment_themes.*

class ThemesFragment : Fragment(R.layout.fragment_themes), DialogClickListener {

    private val TAG = "ThemesFragment"

    private lateinit var viewModel: ThemeViewModel
    private lateinit var themesAdapter: ThemesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_actionbar, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MindActivity).viewModel.themeViewModel
        viewModel.attach()

        setupRecyclerView()
        setFabAction()
        setObserver()
    }

    override fun <T> onDialogClickOkButton(data: T?) {
        showThemeNameDialog(data as Category)
    }

    override fun onDialogClick() {
        viewModel.onDialogClick()
    }

    private fun showThemeNameDialog(category: Category) {
        val dialog = ThemeNameDialog()
        dialog.dialogClickListener = this
        dialog.categoryId = category.id
        dialog.categoryName = category.name
        dialog.show((activity as MindActivity).supportFragmentManager, "")
    }

    override fun onStop() {
        super.onStop()
        viewModel.detach()
    }

    private fun setupRecyclerView() {
        themesAdapter = ThemesAdapter()

        themesAdapter.setOnItemClickListener { theme ->
            onListItemClick(theme)
        }

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

        // show fragment dialog
        fabAddTheme.setOnClickListener {
            val dialog = CategoryChoiceDialog()
            dialog.dialogClickListener = this
            dialog.show((activity as MindActivity).supportFragmentManager, "")
        }

    }

    private fun onListItemClick(theme: Theme) {
        val bundle = Bundle()
        bundle.putSerializable("Theme", theme)
        findNavController().navigate(R.id.action_themesFragment_to_questionsFragment, bundle)
    }

}





























