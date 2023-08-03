package com.begletsov.redbook.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.begletsov.redbook.R
import com.begletsov.redbook.databinding.ItemPlaceBinding
import com.begletsov.redbook.models.Place
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.jetbrains.annotations.Nullable


class PlaceAdapter (private val context: Context) :
    ListAdapter<Place, PlaceAdapter.PlaceViewHolder>(PlaceDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(itemView, parent, context)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaceViewHolder(view: View, private val parent: ViewGroup, private val context: Context) :
        RecyclerView.ViewHolder(view) {
        private val binding: ItemPlaceBinding
        private val navController: NavController

        init {
            binding = ItemPlaceBinding.bind(view)
            navController = Navigation.findNavController(parent)
        }

        fun bind(place: Place) {
            binding.placeName.text = place.description.name
            binding.root.setOnClickListener {
                navController.navigate(R.id.navigation_place)
            }
            if (place.description.imagePath.isEmpty())
                binding.placeItemImage.visibility = View.GONE
            else
                GlideApp.with(parent)
                    .load(place.description.imagePath)
                    .into(binding.placeItemImage)
        }
    }

    object PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
}