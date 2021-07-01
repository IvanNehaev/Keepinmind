package com.nehaev.keepinmind.ui.tests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.ui.fragments.TestsFragment

class TestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tests)
        fragmentInit()
    }

    private fun fragmentInit() {

        supportFragmentManager.beginTransaction().apply {
            add(R.id.tests_fragmentContainer, TestsFragment())
            commit()
        }
    }
}
