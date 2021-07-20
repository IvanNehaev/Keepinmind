package com.nehaev.keepinmind.util

interface DialogClickListener {
    fun <T> onDialogClickOkButton(data: T? = null)
    fun onDialogClick()
}