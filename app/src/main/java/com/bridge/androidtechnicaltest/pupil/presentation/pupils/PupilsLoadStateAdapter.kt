package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bridge.androidtechnicaltest.core.utils.visible
import com.bridge.androidtechnicaltest.databinding.ItemLoadStateBinding

class PupilsLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PupilsLoadStateAdapter.LoadStateViewHolder>(){

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        return LoadStateViewHolder(
            ItemLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class LoadStateViewHolder(private val binding: ItemLoadStateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState){
            binding.apply {
                loadStateProgress.visible(loadState is LoadState.Loading)
                loadStateRetry.visible(loadState is LoadState.Error)

                loadStateRetry.setOnClickListener { retry() }
            }

        }
    }
}