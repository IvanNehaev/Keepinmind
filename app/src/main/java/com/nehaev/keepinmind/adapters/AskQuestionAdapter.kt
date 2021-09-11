package com.nehaev.keepinmind.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.models.Question
import kotlinx.android.synthetic.main.item_flip_card_question_back.view.*
import kotlinx.android.synthetic.main.item_flip_card_question_front.view.*
import kotlinx.android.synthetic.main.item_view_pager_question.view.*

class AskQuestionAdapter : RecyclerView.Adapter<AskQuestionAdapter.AskQuestionViewHolder>() {

    inner class AskQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AskQuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager_question, parent, false)
        return AskQuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AskQuestionViewHolder, position: Int) {
        val curQuestion = differ.currentList[position]
        holder.itemView.item_flip_card_question_front_tvQuestionText.text = curQuestion.question
        holder.itemView.item_flip_card_question_back_tvQuestionText.text = curQuestion.answer
        if (!holder.itemView.item_flip_card_question_front_btnShowAnswer.hasOnClickListeners()) {
            holder.itemView.item_flip_card_question_front_btnShowAnswer.setOnClickListener {
                holder.itemView.item_view_pager_question_easyFlipView.flipTheView()
            }
            holder.itemView.item_flip_card_question_back_btnFalse.setOnClickListener {
                holder.itemView.item_view_pager_question_easyFlipView.flipTheView()
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}