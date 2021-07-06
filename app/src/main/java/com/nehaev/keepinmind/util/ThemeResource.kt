package com.nehaev.keepinmind.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nehaev.keepinmind.models.Theme

sealed class ThemeResource(
        val liveData: LiveData<List<Theme>>? = null,
        val message: String? = null
) {
    class Success(liveData: LiveData<List<Theme>>) : ThemeResource(liveData = liveData)
    class Error(message: String) : ThemeResource(message = message)
    class Loading : ThemeResource()
}