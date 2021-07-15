package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nehaev.keepinmind.repository.MindRepository

class MindViewModelProviderFactory(
    val mindRepository: MindRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MindRepository::class.java)
                .newInstance(mindRepository)
    }
}
