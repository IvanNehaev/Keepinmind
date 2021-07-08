package com.nehaev.keepinmind.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nehaev.keepinmind.models.Theme

sealed class ThemeResource(
        val itemList: List<ThemeListResource>? = null,
        val message: String? = null
) {
    class Success(itemList: List<ThemeListResource>) : ThemeResource(itemList = itemList)
    class Error(message: String) : ThemeResource(message = message)
    class Loading : ThemeResource()
}