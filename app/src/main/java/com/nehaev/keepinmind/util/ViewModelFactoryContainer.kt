package com.nehaev.keepinmind.util

import com.nehaev.keepinmind.models.Test

//sealed class ViewModelFactoryClass<T>(
//    val data: T
//) {
//    class TestViewModelFactory<T>(data: T): ViewModelFactoryClass<T>(data)
//}

class ViewModelFactoryContainer<T>(
    val data: T
)