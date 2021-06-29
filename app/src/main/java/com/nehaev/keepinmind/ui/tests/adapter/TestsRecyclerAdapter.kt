package com.nehaev.keepinmind.ui.tests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.R

class TestsRecyclerAdapter(private val names: List<String>) :
    RecyclerView.Adapter<TestsRecyclerAdapter.TestsViewHolder>() {

    class TestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mTestName: TextView = itemView.findViewById(R.id.tvTestName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestsViewHolder {
        val itemView =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.recyclerview_test_item, parent, false)
        return TestsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TestsViewHolder, position: Int) {
        holder.mTestName.text = names[position]
    }

    override fun getItemCount() = names.size
}
