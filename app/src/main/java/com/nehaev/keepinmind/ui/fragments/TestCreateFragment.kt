package com.nehaev.keepinmind.ui.fragments

import android.app.Activity
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.TestCreateAdapter
import com.nehaev.keepinmind.adapters.ThemesAdapter
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.repository.MindRepository
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
                    mAdapter.differ.submitList(state.listItems)
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
        mAdapter =
            ThemesAdapter(
               list_item_theme_layout = R.layout.list_item_theme_minimize,
               onBindViewHolderAction = ::listThemesBindViewAction
            )

        rv_pickThemes.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun listThemesBindViewAction(itemList: ThemeListResource, holder: ThemesAdapter.ThemesViewHolder, position: Int) {
        when(itemList) {
            is ThemeListResource.ThemeItem -> {
                holder.itemView.apply {
                    list_item_theme_min_cb_theme.text = itemList.theme?.name
                    list_item_theme_min_tv_counter.text = itemList.theme?.questionCnt.toString()
                    Log.d(TAG, "${itemList.theme?.name} (${itemList.theme?.questionCnt})")

                    setOnClickListener {
                        list_item_theme_min_cb_theme.apply {
                            isChecked = !isChecked
                        }
                    }
                }
            }
            is ThemeListResource.CategoryItem -> {
                holder.itemView.apply {
                    rvCategoryName.text = itemList.categoryName
                }
            }
        }
    }
}
