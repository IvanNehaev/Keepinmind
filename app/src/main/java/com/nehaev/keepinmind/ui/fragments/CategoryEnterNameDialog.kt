package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.CategoryEnterNameViewModel
import com.nehaev.keepinmind.ui.viewmodels.MindViewModelProviderFactory
import com.nehaev.keepinmind.util.DialogClickListener
import kotlinx.android.synthetic.main.dialog_enter_name.*

class CategoryEnterNameDialog : DialogFragment() {

    var dialogClickListener: DialogClickListener? = null

    private val TAG = "CategoryEnterNameDialog"

    private lateinit var viewModel: CategoryEnterNameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set dialog style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme)
        // create view model
        createViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupDialog()
        return inflater.inflate(R.layout.dialog_enter_name, null)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogText()
        setObserver()
        setupEditText()
        setupButtonOk()
    }

    private fun createViewModel() {
        //viewModel = (activity as MindActivity).viewModel.categoryEnterNameViewModel
        val mindRepository = MindRepository(MindDatabase(activity as MindActivity))
        val viewModelProviderFactory = MindViewModelProviderFactory(mindRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(CategoryEnterNameViewModel::class.java)
    }

    private fun setupButtonOk() {
        btnOk.setOnClickListener {
            viewModel.onButtonOkClick(etItemName.text.toString())
        }
    }

    private fun setupEditText() {
        etItemName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.onTextChanged(etItemName.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { state ->
            when (state) {
                is CategoryEnterNameViewModel.EnterNameDialogState.ValidName -> {
                    setOkButtonActive()
                }
                is CategoryEnterNameViewModel.EnterNameDialogState.InvalidName -> {
                    setOkButtonDisable()
                }
                is CategoryEnterNameViewModel.EnterNameDialogState.DismissDialog -> {
                    dialogClickListener?.onDialogClick()
                    dismiss()
                }
            }
        })
    }

    private fun setupDialogText() {
        tvEnterNameTitle.text = resources.getString(R.string.enter_category_name_text)
        etItemName.hint = resources.getString(R.string.category_name_text)
    }

    private fun setOkButtonDisable(){
        btnOk.isEnabled = false
    }
    private fun setOkButtonActive() {
        btnOk.isEnabled = true
    }
}