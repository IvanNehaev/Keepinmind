package com.nehaev.keepinmind.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.models.Theme
import kotlinx.android.synthetic.main.list_item_theme.view.*
import kotlinx.android.synthetic.main.list_item_theme.view.tvThemeName
import kotlinx.android.synthetic.main.list_item_theme_minimize.view.*

class TestCreateAdapter : RecyclerView.Adapter<TestCreateAdapter.ThemesViewHolder>() {

    private var mOnItemClickListener: ((Theme, View) -> Unit)? = null

    private val mDifferCallback = object : DiffUtil.ItemCallback<Theme>() {
        override fun areItemsTheSame(oldItem: Theme, newItem: Theme) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Theme, newItem: Theme) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, mDifferCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemesViewHolder {
        return ThemesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_theme_minimize,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ThemesViewHolder, position: Int) {
        val theme = differ.currentList[position]
        holder.itemView.apply {
            list_item_theme_min_cb_theme.text = theme.name
            list_item_theme_min_tv_counter.text = theme.questionCnt.toString()
            setOnClickListener {
                list_item_theme_min_cb_theme.apply {
                    isChecked = !isChecked
                }
                    mOnItemClickListener?.let { onItemClick ->
                    onItemClick(theme, it)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun setOnItemClickListener(listener: (Theme, View) -> Unit) {
        mOnItemClickListener = listener
    }

    inner class ThemesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}