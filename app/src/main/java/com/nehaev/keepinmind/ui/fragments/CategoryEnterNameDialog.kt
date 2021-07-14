package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.CategoriesAdapter
import com.nehaev.keepinmind.ui.viewmodels.CategoryChoiceViewModel
import com.nehaev.keepinmind.ui.viewmodels.CategoryEnterNameViewModel
import kotlinx.android.synthetic.main.fragmentdialog_enter_name.*
import java.util.*

class CategoryEnterNameDialog : DialogFragment() {

    private val TAG = "CategoryEnterNameDialog"

    private lateinit var viewModel: CategoryEnterNameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set dialog style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupDialog()
        return inflater.inflate(R.layout.fragmentdialog_enter_name, null)
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

        viewModel = (activity as MindActivity).viewModel.categoryEnterNameViewModel

        setupDialogText()
        setObserver()
        setupEditText()
        setupButtonOk()
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