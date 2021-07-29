package com.nehaev.keepinmind.util

sealed class ThemeFragmentStates(
    val itemList: List<ThemeListResource>? = null,
    val message: String? = null
) {
    class NewData(itemList: List<ThemeListResource>) : ThemeFragmentStates(itemList = itemList)
    class Loading() : ThemeFragmentStates()
    class Error(message: String) : ThemeFragmentStates(message = message)
}