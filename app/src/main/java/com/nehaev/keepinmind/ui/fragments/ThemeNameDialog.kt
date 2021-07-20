package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.CategoryEnterNameViewModel
import com.nehaev.keepinmind.ui.viewmodels.MindViewModelProviderFactory
import com.nehaev.keepinmind.ui.viewmodels.ThemeNameViewModel
import com.nehaev.keepinmind.util.DialogClickListener
import com.nehaev.keepinmind.util.EditTextDialogStates
import kotlinx.android.synthetic.main.fragmentdialog_enter_name.*

class ThemeNameDialog : DialogFragment() {

    var dialogClickListener: DialogClickListener? = null

    private val TAG = "ThemeNameDialog"

    private lateinit var viewModel: ThemeNameViewModel

    lateinit var categoryId: String
    lateinit var categoryName: String

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

    private fun createViewModel() {

        val mindRepository = MindRepository(MindDatabase(activity as MindActivity))
        val viewModelProviderFactory = MindViewModelProviderFactory(mindRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ThemeNameViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogText()
        setupEditText()
        setupButtonOk()
        setObserver()
    }

    private fun setupButtonOk() {
        btnOk.setOnClickListener {
            viewModel.onButtonOkClick(themeName = etItemName.text.toString(),
                                        categoryName = categoryName,
                                        categoryId = categoryId)
        }
    }

    private fun setupEditText() {
        etItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onTextChanged(etItemName.text.toString())
            }
        })
    }

    private fun setupDialogText() {
        tvEnterNameTitle.text = resources.getString(R.string.enter_theme_name_text)
        etItemName.hint = resources.getString(R.string.theme_name_text)
    }

    private fun setObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            when(state) {
                is EditTextDialogStates.ValidName -> {
                    setOkButtonActive()
                }
                is EditTextDialogStates.InvalidName -> {
                    setOkButtonDisable()
                }
                is EditTextDialogStates.DismissDialog -> {
                    dialogClickListener?.onDialogClick()
                    dismiss()
                }
            }
        })
    }

    private fun setOkButtonDisable(){
        btnOk.isEnabled = false
    }
    private fun setOkButtonActive() {
        btnOk.isEnabled = true
    }
}