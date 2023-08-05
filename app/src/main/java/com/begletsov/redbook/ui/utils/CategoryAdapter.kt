package com.begletsov.redbook.ui.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
        return CategoryViewHolder(itemView, parent, context)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(view: View, parent: ViewGroup, private val context: Context) :
        RecyclerView.ViewHolder(view) {
        private val binding: ItemCategoryBinding
        private val navController: NavController

        init {
            binding = ItemCategoryBinding.bind(view)
            navController = Navigation.findNavController(parent)
        }

        fun bind(category: Category) {
            binding.categoryName.text = category.name
//            if (category.iconFilePath.isEmpty())
//                binding.categoryIcon.visibility = GONE
//            else
//                GlideApp
//                    .with(context)
//                    .load(category.iconFilePath)
//                    .into(binding.categoryIcon)
            binding.categoryIcon.setImageResource(category.iconFilePath)
            val bundle = Bundle()

            bundle.putString("id", category.id.toString())
            binding.root.setOnClickListener {
                navController.navigate(R.id.navigation_choose_place, bundle)
            }
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