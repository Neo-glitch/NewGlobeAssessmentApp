package com.bridge.androidtechnicaltest.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bridge.androidtechnicaltest.core.presentation.model.ImageSource
import com.bridge.androidtechnicaltest.core.presentation.model.ImageSourceType
import com.bridge.androidtechnicaltest.databinding.ItemImageSourceBinding

class ImageSourceAdapter(
    private val onItemClicked: (ImageSourceType) -> Unit
): ListAdapter<ImageSource, ImageSourceAdapter.ImageSourceViewHolder>(IMAGE_SOURCE_DIFF_CALLBACK) {

    companion object {
        private val IMAGE_SOURCE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<ImageSource>() {
            override fun areItemsTheSame(
                oldItem: ImageSource,
                newItem: ImageSource
            ): Boolean {
                return oldItem.description == newItem.description
            }

            override fun areContentsTheSame(
                oldItem: ImageSource,
                newItem: ImageSource
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSourceViewHolder {
        val binding = ItemImageSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSourceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ImageSourceViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class ImageSourceViewHolder(private val binding: ItemImageSourceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageSource) {
            binding.sourceIcon.setImageResource(item.icon)
            binding.galleryText.text = item.description
            binding.root.setOnClickListener { onItemClicked(item.sourceType) }
        }

    }

}