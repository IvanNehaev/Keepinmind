package com.nehaev.keepinmind.util

import com.nehaev.keepinmind.ui.viewmodels.CategoryEnterNameViewModel

sealed class EditTextDialogStates {
    object ValidName : EditTextDialogStates()
    object InvalidName : EditTextDialogStates()
    object DismissDialog : EditTextDialogStates()
}