package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class CategoryEnterNameViewModel(
    val mindRepository: MindRepository
) : ViewModel() {

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
        saveCategory(text)
    }

    private fun saveCategory(name: String) {
        viewModelScope.launch {
            async { mindRepository.categories.upsertCategory(Category(UUID.randomUUID().toString(), name)) }.await()
            liveData.postValue(EnterNameDialogState.DismissDialog)
        }
    }
}