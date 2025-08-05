package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.core.utils.loadImage
import com.bridge.androidtechnicaltest.databinding.ItemPupilBinding
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

class PupilsAdapter(
    private val onItemClick: (PupilEntity) -> Unit
):  PagingDataAdapter<PupilEntity, PupilsAdapter.PupilsViewHolder>(PUPILS_COMPARATOR){

    companion object {
        val PUPILS_COMPARATOR = object : DiffUtil.ItemCallback<PupilEntity>() {
            override fun areItemsTheSame(
                oldItem: PupilEntity,
                newItem: PupilEntity
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: PupilEntity,
                newItem: PupilEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PupilsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPupilBinding.inflate(inflater, parent, false)
        return PupilsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PupilsViewHolder,
        position: Int
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PupilsViewHolder(private val binding: ItemPupilBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pupil: PupilEntity) {
            binding.pupilNameTv.text = pupil.name
            binding.pupilCountry.text = pupil.country
            binding.pupilImage.loadImage(pupil.image, errorImageRes = R.drawable.ic_user)
            binding.root.setOnClickListener { onItemClick(pupil) }
        }

    }
}