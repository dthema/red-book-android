package com.begletsov.redbook.ui.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.begletsov.redbook.R
import com.begletsov.redbook.databinding.ItemCategoryBinding
import com.begletsov.redbook.models.Category
import com.bumptech.glide.Glide

class CategoryAdapter(private val context: Context) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(view: View, private val context: Context) :
        RecyclerView.ViewHolder(view) {
        private val binding: ItemCategoryBinding

        init {
            binding = ItemCategoryBinding.bind(view)
        }

        fun bind(category: Category) {
            binding.categoryName.text = category.name
            if (category.iconFilePath.isEmpty())
                binding.categoryIcon.visibility = GONE
            else
                Glide.with(context).load(category.iconFilePath).into(binding.categoryIcon)
        }
    }

    object CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}