package com.nehaev.keepinmind.util

import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.models.Theme

sealed class ThemeListResource(
    val categoryName: String? = null,
    val theme: Theme? = null
) {
    class ThemeItem(theme: Theme) : ThemeListResource(theme = theme)
    class CategoryItem(name: String) : ThemeListResource(categoryName = name)
}