package com.nehaev.keepinmind.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.models.Test
import kotlinx.android.synthetic.main.list_item_test.view.*

class TestsAdapter : RecyclerView.Adapter<TestsAdapter.TestsViewHolder>() {

    inner class TestsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val  differCallback = object : DiffUtil.ItemCallback<Test>() {
        override fun areItemsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestsViewHolder {
        return TestsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_test,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: TestsViewHolder, position: Int) {
        val test = differ.currentList[position]
        holder.itemView.apply {
            tvThemeName.text = test.name
            tvQuestionCount.text = test.questionCnt.toString()
            tvTestRate.text = "${test.rate.toString()}%"
            setOnClickListener {
                onItemClickListener?.let { it(test) }
            }
        }
    }

    private var onItemClickListener: ((Test) -> Unit)? = null

    fun setOnItemClickListener(listener: (Test) -> Unit) {
        onItemClickListener = listener
    }
}