package com.nehaev.keepinmind.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.models.Category
import kotlinx.android.synthetic.main.list_item_category_create.view.*

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_category_create,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.itemView.apply {
            tvCategoryName.text = category.name
            setOnClickListener {
                onItemClickListener?.let {
                    it(category)
                }
            }
        }
    }

    var onItemClickListener: ((Category) -> Unit)? = null

    override fun getItemCount() = differ.currentList.size
}