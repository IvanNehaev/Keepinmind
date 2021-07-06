package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.ui.viewmodels.MindViewModel
import com.nehaev.keepinmind.adapters.TestsAdapter
import com.nehaev.keepinmind.util.Resource
import kotlinx.android.synthetic.main.fragment_tests.*

class TestsFragment : Fragment(R.layout.fragment_tests) {

    lateinit var viewModel: MindViewModel
    lateinit var testsAdapter: TestsAdapter

    private val TAG = "TestFragment"

    private var nameCounter = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MindActivity).viewModel
        setupRecyclerView()
        setFabAction()

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
        rvTests.apply {
            adapter = testsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setFabAction() {
        fabAddTest.setOnClickListener {
            nameCounter++
            Snackbar.make(rvTests, "Added Name #$nameCounter", Snackbar.LENGTH_SHORT).show()
            viewModel.upsertTest(
                test =
                    Test(id =nameCounter,
                        name = "$nameCounter",
                        itemTableName = "name$nameCounter",
                        questionCnt = 4 + nameCounter,
                        rate = 55))
        }
    }

//    var testsNames: MutableList<String> = mutableListOf("Kotlin", "Java", "C++", "Swift")
//    var displayList: MutableList<String> = mutableListOf()
//    var nameCounter = 4
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        displayList.addAll(testsNames)
//        fillRecyclerView()
//        setFabAction()
//        setExploreTestAction()
//    }
//
//    private fun setExploreTestAction() {
//
//        etExploreTests.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {}
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//                if (etExploreTests.text.isEmpty()) {
//                    displayList.clear()
//                    displayList.addAll(testsNames)
//                } else {
//                    var newDisplayList = testsNames.filter {
//                        it.contains(etExploreTests.text) || it.contains(etExploreTests.text.toString().capitalize())
//                    }
//
//                    displayList.clear()
//                    displayList.addAll(newDisplayList)
//                }
//                rvTests.adapter?.notifyDataSetChanged()
//                rvTests.smoothScrollToPosition(0)
//            }
//        })
//    }
//
//    private fun setFabAction() {
//        fabAddTest.setOnClickListener {
//            nameCounter++
//            Snackbar.make(rvTests, "Name #$nameCounter,  size: ${rvTests.size}", Snackbar.LENGTH_SHORT).show()
//            testsNames.add("#$nameCounter language")
//            displayList.add("#$nameCounter language")
//            rvTests.adapter?.notifyDataSetChanged()
//            rvTests.smoothScrollToPosition(displayList.size)
//        }
//    }
//
//    private fun fillRecyclerView() {
//
//        rvTests.layoutManager = LinearLayoutManager(requireContext())
//        rvTests.adapter = TestsRecyclerAdapter(displayList)
//    }
}