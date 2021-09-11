package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.ViewModelFactoryContainer

class MyViewModelProviderFactory<S>(
    val mindRepository: MindRepository,
    val container: ViewModelFactoryContainer<S>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MindRepository::class.java, ViewModelFactoryContainer::class.java)
            .newInstance(mindRepository, container)
    }
}