package com.nehaev.keepinmind.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.models.Question
import kotlinx.android.synthetic.main.list_item_question.view.*

class QuestionsAdapter : RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Question, newItem: Question) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((Question) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        return QuestionsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_question,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        val question = differ.currentList[position]
        holder.itemView.apply {
            tvQuestionName.text = question.question
            tvQuestionPosition.text = (position + 1).toString()
            setOnClickListener {
                onItemClickListener?.let { onItemClick ->
                    onItemClick(question)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun setOnItemClickListener(listener: (Question) -> Unit) {
        onItemClickListener = listener
    }

    inner class QuestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
