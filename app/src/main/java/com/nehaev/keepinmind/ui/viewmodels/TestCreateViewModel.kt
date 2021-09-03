package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.ThemeListResource
import com.nehaev.keepinmind.util.ThemesItemListHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class TestCreateViewModel(
    val mindRepository: MindRepository
) : ViewModel() {

    val liveData: MutableLiveData<TestCreateStates> = MutableLiveData()
    private var mSelectedThemes = mutableSetOf<Theme>()
    private lateinit var mThemesList: List<Theme>
    private var mTestName = ""
    var editableTest: Test? = null
        set(value) {
            field = value
            if (value == null)
                getThemes() // get all themes if we create new test
            else {
                getSelectedThemes(value) // get selected themes if we edit existed test
            }
        }

    private fun getSelectedThemes(test: Test) = viewModelScope.launch {
        // set loading status on view
        liveData.postValue(TestCreateStates.Loading())
        // get list of selected themes id
        var themesIdList =
            async { mindRepository.selectedThemesRepository.getThemesId(test.itemTableName) }
        // get themes by id
        val selectedThemesList = mutableListOf<Theme>()
        themesIdList.await().let { list ->
            for (id in list) {
                val theme = async { mindRepository.themes.getThemeById(id) }
                selectedThemesList.add(theme.await())
            }
        }
        // get all themes from db
        var allThemes = listOf<Theme>()
        async {
            allThemes = mindRepository.themes.getAllThemes().filter { theme ->
                theme.questionCnt > 0
            }
        }.await()
        // save themes from db to local list
        mThemesList = allThemes
        // save themes from db to local list of selected themes
        mSelectedThemes = selectedThemesList.toMutableSet()
        // save editable test name to local variable
        editableTest?.let {
            mTestName = it.name
        }
        // send themes list to view
        liveData.postValue(
            TestCreateStates.Success(
                ThemesItemListHelper.listToResourceListWithSelectedItems(
                    allThemes,
                    selectedThemesList
                )
            )
        )
    }

    private fun getThemes() = viewModelScope.launch {
        // set loading status on view
        liveData.postValue(TestCreateStates.Loading())
        // get all themes from db
        var response = listOf<Theme>()
        async { response = mindRepository.themes.getAllThemes() }.await()
        // send response to view
        if (response.isEmpty()) {
            liveData.postValue(TestCreateStates.EmptyList())
        } else {
            // get only none empty themes
            mThemesList = response.filter { theme ->
                theme.questionCnt > 0
            }
            liveData.postValue(
                TestCreateStates.Success(ThemesItemListHelper.listToResourcesList(mThemesList))
            )
        }
    }

    private fun validateTest() {
        if (mTestName.isNotBlank() && mSelectedThemes.size > 0)
            liveData.postValue(TestCreateStates.ValidTest())
        else
            liveData.postValue(TestCreateStates.InvalidTest())
    }

    fun onThemeClick(theme: Theme, isPressed: Boolean) {
        if (isPressed) { // if theme was selected, add them to selected themes list
            mSelectedThemes.add(theme)
        } else { // remove theme from selected themes list
            mSelectedThemes.remove(theme)
        }
        validateTest()
    }

    fun onCategoryClick(categoryName: String) {

    }

    fun onTestNameChanged(name: String) {
        mTestName = name
        validateTest()
    }

    private fun countQuestionInTest(themes: Set<Theme>) = themes.fold(0) { accumulator, theme ->
        accumulator + theme.questionCnt
    }

    fun onButtonSaveClick() {
        val id = UUID.randomUUID().toString()
        // create new test or edit existed
        val newTest = Test(
            id = editableTest?.id ?: id,
            itemTableName = editableTest?.itemTableName ?: "[$id]",
            name = mTestName,
            questionCnt = countQuestionInTest(mSelectedThemes),
            rate = 0
        )
        viewModelScope.launch {
            async {
                // add new/update test in database
                mindRepository.tests.upsert(newTest)
                // if we don't edit existed test, create new table
                if (editableTest == null) {
                    // create new table in database for saving selected themes id
                    mindRepository.selectedThemesRepository.addTable(newTest.itemTableName)
                } else {
                    mindRepository.selectedThemesRepository.clearTable(newTest.itemTableName)
                }
                // add selected themes id in database
                mindRepository.selectedThemesRepository.insertThemes(
                    newTest.itemTableName,
                    mSelectedThemes.toList()
                )
            }.await()
            liveData.postValue(TestCreateStates.SaveTest())
        }
    }

    fun onItemListReceiving() {
        validateTest()
    }

    inner class TestListResource(
        val listItems: List<ThemeListResource>? = null,
        val isChecked: Boolean? = null
    )

    sealed class TestCreateStates(
        val listItems: List<ThemeListResource>? = null
    ) {
        class Loading : TestCreateStates()
        class Success(items: List<ThemeListResource>) : TestCreateStates(listItems = items)
        class EmptyList() : TestCreateStates()
        class SaveTest : TestCreateStates()
        class ValidTest : TestCreateStates()
        class InvalidTest : TestCreateStates()
    }
}