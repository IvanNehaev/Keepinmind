package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.ThemesAdapter
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.ui.viewmodels.MindViewModelProviderFactory
import com.nehaev.keepinmind.ui.viewmodels.TestCreateViewModel
import com.nehaev.keepinmind.util.ThemeListResource
import kotlinx.android.synthetic.main.fragment_create_test.*
import kotlinx.android.synthetic.main.list_item_category.view.*
import kotlinx.android.synthetic.main.list_item_theme_minimize.view.*

class TestCreateFragment : Fragment(R.layout.fragment_create_test) {

    private val TAG = TestCreateFragment::class.simpleName

    private lateinit var mViewModel: TestCreateViewModel
    private lateinit var mAdapter: ThemesAdapter
    private var mEditableTest: Test? = null
    private var mIsEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mEditableTest = arguments?.getSerializable("Test") as Test?
        // set edit mode flag
        mEditableTest?.let { mIsEditMode = true }
        createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setObserver()
        setupTestNameEditText()
        setupButtonSave()
    }

    private fun setupButtonSave() {
        fragment_create_test_btnSave.setOnClickListener {
            mViewModel.onButtonSaveClick()
        }
    }

    private fun setupTestNameEditText() {
        fragment_create_test_etTestName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                mViewModel.onTestNameChanged(fragment_create_test_etTestName.text.toString())
            }
        })

        mEditableTest?.let { test ->
            fragment_create_test_etTestName.setText(test.name, TextView.BufferType.EDITABLE)
        }
    }

    private fun setObserver() {
        mViewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                // Loading themes
                is TestCreateViewModel.TestCreateStates.Loading -> {
                    disableSaveButton()
                }
                // Display themes
                is TestCreateViewModel.TestCreateStates.Success -> {
                    mAdapter.differ.submitList(state.listItems)
                    // notify view model about receiving items list
                    onItemListReceiving()
                }
                // Empty themes list
                is TestCreateViewModel.TestCreateStates.EmptyList -> {
                    disableSaveButton()
                }
                // Save test
                is TestCreateViewModel.TestCreateStates.SaveTest -> {
                    findNavController().navigate(R.id.testsFragment)
                }
                // Valid test
                is TestCreateViewModel.TestCreateStates.ValidTest -> {
                    enableSaveButton()
                }
                // Invalid Test
                is TestCreateViewModel.TestCreateStates.InvalidTest -> {
                    disableSaveButton()
                }
            }
        })
    }

    private fun onItemListReceiving() {
        mViewModel.onItemListReceiving()
    }

    private fun disableSaveButton() {
        fragment_create_test_btnSave.isEnabled = false
    }

    private fun enableSaveButton() {
        fragment_create_test_btnSave.isEnabled = true
    }

    private fun createViewModel() {
        val mindRepository = (activity as MindActivity).mindRepository
        val viewModelFactory = MindViewModelProviderFactory(mindRepository)
        mViewModel = ViewModelProvider(this, viewModelFactory).get(TestCreateViewModel::class.java)
        mViewModel.editableTest = mEditableTest
    }

    private fun setupRecyclerView() {
        mAdapter =
            ThemesAdapter(
                list_item_theme_layout = R.layout.list_item_theme_minimize,
                onBindViewHolderAction = ::listThemesBindViewAction
            )

        fragment_create_test_rvPickThemes.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun listThemesBindViewAction(
        itemList: ThemeListResource,
        holder: ThemesAdapter.ThemesViewHolder,
        position: Int
    ) {
        when (itemList) {
            is ThemeListResource.ThemeItem -> {
                holder.itemView.apply {
                    list_item_theme_min_cb_theme.text = itemList.theme?.name
                    list_item_theme_min_tv_counter.text = itemList.theme?.questionCnt.toString()
                    Log.d(TAG, "${itemList.theme?.name} (${itemList.theme?.questionCnt})")

                    // if we edit already existed test, set checkbox
                    if (mIsEditMode) {
                        list_item_theme_min_cb_theme.isChecked = true
                    }

                    list_item_theme_min_cb_theme.setOnCheckedChangeListener { compoundButton, isChecked ->
                        itemList.theme?.let { theme ->
                            mViewModel.onThemeClick(theme, list_item_theme_min_cb_theme.isChecked)
                        }
                    }

                    setOnClickListener {
                        // change checkbox state
                        list_item_theme_min_cb_theme.apply {
                            isChecked = !isChecked
                        }
                        itemList.theme?.let { theme ->
                            mViewModel.onThemeClick(theme, list_item_theme_min_cb_theme.isChecked)
                        }
                    }
                }
            }
            is ThemeListResource.CategoryItem -> {
                holder.itemView.apply {
                    rvCategoryName.text = itemList.categoryName

                    setOnClickListener {
                        itemList.categoryName?.let { name ->
                            mViewModel.onCategoryClick(name)
                        }
                    }
                }
            }
        }
    }
}
