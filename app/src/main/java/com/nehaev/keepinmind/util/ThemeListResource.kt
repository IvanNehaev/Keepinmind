package com.nehaev.keepinmind.util

import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.models.Theme

sealed class ThemeListResource(
    val categoryName: String? = null,
    val theme: Theme? = null,
    val isSelected: Boolean? = null
) {
    class ThemeItem(theme: Theme, isSelected: Boolean) : ThemeListResource(theme = theme, isSelected = isSelected)
    class CategoryItem(name: String) : ThemeListResource(categoryName = name)
}