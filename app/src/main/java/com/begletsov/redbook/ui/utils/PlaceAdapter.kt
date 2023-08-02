package com.begletsov.redbook.ui.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.begletsov.redbook.R
import com.begletsov.redbook.databinding.ItemPlaceBinding
import com.begletsov.redbook.models.Place
import com.bumptech.glide.Glide

class PlaceAdapter (private val context: Context) :
    ListAdapter<Place, PlaceAdapter.PlaceViewHolder>(PlaceDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaceViewHolder(view: View, private val context: Context) :
        RecyclerView.ViewHolder(view) {
        private val binding: ItemPlaceBinding

        init {
            binding = ItemPlaceBinding.bind(view)
        }

        fun bind(place: Place) {
            binding.placeName.text = place.description.name
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