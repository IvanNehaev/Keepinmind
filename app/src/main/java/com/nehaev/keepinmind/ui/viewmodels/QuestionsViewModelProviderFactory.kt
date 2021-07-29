package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository

class QuestionsViewModelProviderFactory(
    val mindRepository: MindRepository,
    val theme: Theme
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MindRepository::class.java, Theme::class.java)
                .newInstance(mindRepository, theme)
    }
}