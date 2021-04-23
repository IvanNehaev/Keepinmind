package com.nehaev.keepinmind.ui.tests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.ui.tests.adapter.TestsRecyclerAdapter


class TestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tests)

        val listTests = listOf<String>("Kotlin", "Java", "C++", "Swift")

        val recyclerView: RecyclerView = findViewById(R.id.rvTests)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestsRecyclerAdapter(listTests)


    }
}