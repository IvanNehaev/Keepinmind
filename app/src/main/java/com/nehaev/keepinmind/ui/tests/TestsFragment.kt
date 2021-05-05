package com.nehaev.keepinmind.ui.tests

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.ui.tests.adapter.TestsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_tests.*

class TestsFragment : Fragment() {

    var testsNames: MutableList<String> = mutableListOf("Kotlin", "Java", "C++", "Swift")
    var displayList: MutableList<String> = mutableListOf()
    var nameCounter = 4


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayList.addAll(testsNames)

        fillRecyclerView()

        setFabAction()

        setExploreTestAction()
    }

    private fun setExploreTestAction() {

        etExploreTests.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (etExploreTests.text.isEmpty()) {
                    displayList.clear()
                    displayList.addAll(testsNames)
                } else {
                    var newDisplayList = testsNames.filter {
                        it.contains(etExploreTests.text) || it.contains(etExploreTests.text.toString().capitalize())
                    }

                    displayList.clear()
                    displayList.addAll(newDisplayList)
                }
                rvTests.adapter?.notifyDataSetChanged()
                rvTests.smoothScrollToPosition(0)
            }
        })
    }

    private fun setFabAction() {
        fabAddTest.setOnClickListener {
            nameCounter++
            Snackbar.make(rvTests, "Name #$nameCounter,  size: ${rvTests.size}", Snackbar.LENGTH_SHORT).show()
            testsNames.add("#$nameCounter language")
            displayList.add("#$nameCounter language")
            rvTests.adapter?.notifyDataSetChanged()
            rvTests.smoothScrollToPosition(displayList.size)
        }
    }

    private fun fillRecyclerView() {

        rvTests.layoutManager = LinearLayoutManager(requireContext())
        rvTests.adapter = TestsRecyclerAdapter(displayList)
    }
}