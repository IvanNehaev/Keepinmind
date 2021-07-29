package com.nehaev.keepinmind.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nehaev.keepinmind.models.Category

class SimpleAdapter<T> (
    val differCallBack : DiffUtil.ItemCallback<T>
        ): RecyclerView.Adapter<SimpleAdapter<T>.SimpleViewHolder>() {

    inner class SimpleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    val differ = AsyncListDiffer( this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}