package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.EditTextDialogStates
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ThemeNameViewModel(
    val mindRepository: MindRepository
) : ViewModel() {

    val liveData: MutableLiveData<EditTextDialogStates> = MutableLiveData()

    init {
        liveData.postValue(EditTextDialogStates.InvalidName)
    }

    fun onTextChanged(text: String) {
        if (text.isNotBlank()) {
            liveData.postValue(EditTextDialogStates.ValidName)
        } else {
            liveData.postValue(EditTextDialogStates.InvalidName)
        }
    }

    fun onButtonOkClick(themeName: String,
                        categoryId: String,
                        categoryName: String) {
        saveTheme(name = themeName,
            categoryId = categoryId,
            categoryName = categoryName)
    }

    private fun saveTheme(name: String,
                          categoryId: String,
                          categoryName: String) {
        viewModelScope.launch {
            async {
                val newTheme = Theme(UUID.randomUUID().toString(), categoryId, categoryName, name, 0)
                mindRepository.themes.upsertTheme(newTheme)
            }.await()
            liveData.postValue(EditTextDialogStates.DismissDialog)
        }
    }

}