package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.ui.viewmodels.MindViewModel
import com.nehaev.keepinmind.adapters.TestsAdapter
import com.nehaev.keepinmind.adapters.ThemesAdapter
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.util.Resource
import com.nehaev.keepinmind.util.ThemeListResource
import kotlinx.android.synthetic.main.fragment_tests.*
import kotlinx.android.synthetic.main.list_item_category.view.*
import kotlinx.android.synthetic.main.list_item_theme.view.*
import kotlinx.android.synthetic.main.list_item_theme_minimize.view.*

class TestsFragment : Fragment(R.layout.fragment_tests) {

    lateinit var viewModel: MindViewModel
    lateinit var testsAdapter: TestsAdapter

    private val TAG = TestsFragment::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        setupRecyclerView()
        setFabAction()
        setObserver()
    }

    private fun createViewModel(){
        viewModel = (activity as MindActivity).viewModel
    }

    private fun setObserver() {
        viewModel.testsLiveData.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response?.let {testsResponse ->
                        testsAdapter.differ.submitList(testsResponse.data)

                        viewModel.testsDbLiveData.observe(viewLifecycleOwner, Observer {
                            testsAdapter.differ.submitList(it)
                        })
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {

    }

    private fun hideProgressBar() {

    }

    private fun setupRecyclerView() {
        testsAdapter = TestsAdapter()
        testsAdapter.setOnItemClickListener { test ->
            onTestClick(test)
        }

        rvTests.apply {
            adapter = testsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun onTestClick(test: Test) {
        val bundle = Bundle()
        bundle.putSerializable("Test", test)
        findNavController().navigate(R.id.action_testsFragment_to_createTestFragment, bundle)
    }

    private fun setFabAction() {
        fabAddTest.setOnClickListener {
            findNavController().navigate(R.id.action_testsFragment_to_createTestFragment)
        }
    }

    private fun addFakeTest() {

        Snackbar.make(rvTests, "Added Name #1", Snackbar.LENGTH_SHORT).show()
        viewModel.upsertTest(
            test =
            Test(id ="1",
                name = "1",
                itemTableName = "name 1",
                questionCnt = 4 + 1,
                rate = 55))
    }
}