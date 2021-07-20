package com.nehaev.keepinmind.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.CategoriesAdapter
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.CategoryChoiceViewModel
import com.nehaev.keepinmind.ui.viewmodels.CategoryEnterNameViewModel
import com.nehaev.keepinmind.ui.viewmodels.MindViewModelProviderFactory
import com.nehaev.keepinmind.util.DialogClickListener
import com.nehaev.keepinmind.util.Resource
import kotlinx.android.synthetic.main.fragmentdialog_choice_categoty.*

class CategoryChoiceDialog : DialogFragment(), DialogClickListener {

    private val TAG = "CategoryChoiceDialog"

    private lateinit var viewModel: CategoryChoiceViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    var dialogClickListener: DialogClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set dialog style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme)
        createViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupDialog()
        return inflater.inflate(R.layout.fragmentdialog_choice_categoty, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        setupRecyclerView()
        setObserver()
        setOnButtonCancelClick()
    }

    private fun createViewModel() {
        //viewModel = (activity as MindActivity).viewModel.categoryEnterNameViewModel
        val mindRepository = MindRepository(MindDatabase(activity as MindActivity))
        val viewModelProviderFactory = MindViewModelProviderFactory(mindRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(CategoryChoiceViewModel::class.java)
    }

    override fun <T> onDialogClickOkButton(data: T?) {

    }

    override fun onDialogClick() {
        viewModel.onDialogSaveClick()
    }

    private fun setOnButtonCancelClick() {
        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun setupDialog() {
        val window = dialog?.window
        // set bottom gravity
        window?.setGravity(Gravity.BOTTOM or Gravity.CENTER)
        // set width to MATCH PARENT
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onStop() {
        super.onStop()
    }

    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        rvChoiceCategory.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        categoriesAdapter.onItemClickListener = ::onCategoryClick
    }

    private fun onCategoryClick(category: Category) {
        if (category.id == "0") {
            onNewCategoryClick()
        } else {
            dismiss()
            dialogClickListener?.onDialogClickOkButton(category)
        }
    }

    private fun showThemeNameDialog() {
        val dialog = ThemeNameDialog()
        dialog.show((activity as MindActivity).supportFragmentManager, "")
    }

    private fun onNewCategoryClick() {
        val dialog = CategoryEnterNameDialog()
        dialog.dialogClickListener = this
        dialog.show((activity as MindActivity).supportFragmentManager, "")
    }

    private fun setObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    onSuccessResponse(response.data)
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    viewErrorState()
                }
            }
        })
    }

    private fun onSuccessResponse(response: List<Category>?) {
        viewSuccessState()
        response?.let { categories ->
            categories.toMutableList()[0] = Category(
                id = "0",
                name = resources.getString(R.string.new_category_text)
            )
            categoriesAdapter.differ.submitList(categories)
        }
    }

    private fun viewSuccessState() {

        rvChoiceCategory.visibility = View.VISIBLE
    }

    private fun viewErrorState() {

        rvChoiceCategory.visibility = View.INVISIBLE
    }
}