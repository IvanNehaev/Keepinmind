package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nehaev.keepinmind.R
import kotlinx.android.synthetic.main.fragmentdialog_enter_name.*

class ThemeNameDialog : DialogFragment() {

    private val TAG = "ThemeNameDialog"

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

        setupDialogText()
    }

    private fun setupDialogText() {
        tvEnterNameTitle.text = resources.getString(R.string.enter_theme_name_text)
        etItemName.hint = resources.getString(R.string.theme_name_text)
    }
}