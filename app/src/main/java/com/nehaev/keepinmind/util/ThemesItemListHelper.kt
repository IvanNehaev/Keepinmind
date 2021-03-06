package com.nehaev.keepinmind.util

import com.nehaev.keepinmind.models.Theme

class ThemesItemListHelper {
    companion object {
        fun listToResourcesList(themes: List<Theme>): List<ThemeListResource> {
            val listItems = mutableListOf<ThemeListResource>()
            val categories = mutableSetOf<String>()
            // collect all unic categories names
            for(theme in themes)
                categories.add(theme.categoryName)

            for (category in categories) {
                // add category name in itemList
                listItems.add(ThemeListResource.CategoryItem(category))
                // get themes included in current category
                val themesInCategory = themes.filter { theme ->
                    theme.categoryName == category
                }
                // add theme in itemList
                themesInCategory.forEach {
                    listItems.add(ThemeListResource.ThemeItem(it, false))
                }
            }
            return listItems
        }
        fun listToResourceListWithSelectedItems(
            allThemes: List<Theme>,
            selectedThemes: List<Theme>
        ): List<ThemeListResource> {
            val listItems = mutableListOf<ThemeListResource>()
            val categories = mutableSetOf<String>()
            // collect all unic categories names
            for(theme in allThemes)
                categories.add(theme.categoryName)

            for (category in categories) {
                // add category name in itemList
                listItems.add(ThemeListResource.CategoryItem(category))
                // get themes included in current category
                val themesInCategory = allThemes.filter { theme ->
                    theme.categoryName == category
                }
                // add theme in itemList
                themesInCategory.forEach { theme: Theme ->
                    val isSelected = selectedThemes.contains(theme)
                    listItems.add(ThemeListResource.ThemeItem(theme, isSelected))
                }
            }
            return listItems
        }
    }
}