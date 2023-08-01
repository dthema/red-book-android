package com.begletsov.redbook.ui.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.begletsov.redbook.R
import com.begletsov.redbook.databinding.CategoryItemBinding
import com.begletsov.redbook.databinding.ItemCategoryBinding
import com.begletsov.redbook.models.Category

class CategoryAdapter (private val context: Context) :
    ListAdapter<Category, CategoryAdapter.OrderViewHolder>(OrderDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return OrderViewHolder(itemView, viewType, context)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(view: View, viewType: Int, private val context: Context) :
        RecyclerView.ViewHolder(view) {
        val binding: ItemCategoryBinding? = null

        fun bind(category: Category?) {

        }
    }

    object OrderDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}