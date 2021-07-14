package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CategoryEnterNameViewModel(
    private val mindRepository: MindRepository,
    private val viewModelScope: CoroutineScope
) {

    sealed class EnterNameDialogState {
        object ValidName : EnterNameDialogState()
        object InvalidName : EnterNameDialogState()
        object DismissDialog : EnterNameDialogState()
    }

    val liveData: MutableLiveData<EnterNameDialogState> = MutableLiveData()

    init {
        liveData.postValue(EnterNameDialogState.InvalidName)
    }

    fun onTextChanged(text: String) {
        if (text.isNotBlank()) {
            liveData.postValue(EnterNameDialogState.ValidName)
        } else {
            liveData.postValue(EnterNameDialogState.InvalidName)
        }
    }

    fun onButtonOkClick(text: String) {
        liveData.postValue(EnterNameDialogState.DismissDialog)
        saveCategory(text)
    }

    private fun saveCategory(name: String) {
        viewModelScope.launch {
            mindRepository.categories.upsertCategory(Category(2, name))
        }
    }
}