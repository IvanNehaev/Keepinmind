package com.nehaev.keepinmind.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.TestCreateAdapter
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.MindViewModelProviderFactory
import com.nehaev.keepinmind.ui.viewmodels.TestCreateViewModel
import kotlinx.android.synthetic.main.fragment_create_test.*

class TestCreateFragment : Fragment(R.layout.fragment_create_test) {

    private lateinit var mViewModel: TestCreateViewModel
    private lateinit var mAdapter: TestCreateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setObserver()
    }

    private fun setObserver() {
        mViewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is TestCreateViewModel.TestCreateStates.Loading -> {

                }
                is TestCreateViewModel.TestCreateStates.Success -> {
                    mAdapter.differ.submitList(state.themes)
                }
                is TestCreateViewModel.TestCreateStates.EmptyList -> {

                }
            }
        })
    }

    private fun createViewModel() {
        val mindRepository = MindRepository(MindDatabase(activity as MindActivity))

        val viewModelFactory = MindViewModelProviderFactory(mindRepository)
        mViewModel = ViewModelProvider(this, viewModelFactory).get(TestCreateViewModel::class.java)
    }

    private fun setupRecyclerView() {
        mAdapter = TestCreateAdapter()

        rv_pickThemes.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
