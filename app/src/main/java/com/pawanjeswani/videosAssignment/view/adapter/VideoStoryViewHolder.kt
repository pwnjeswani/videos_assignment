package com.pawanjeswani.videosAssignment.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pawanjeswani.videosAssignment.databinding.ItemVideoStoryBinding
import com.pawanjeswani.videosAssignment.model.Hit

class VideoStoryViewHolder(private val binding: ItemVideoStoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(field: Hit, listener: StoryClickListener) {
        field.apply {
            binding.let {
                binding.pbStory.progress = (0..100).random()
                Glide.with(binding.root.context)
                    .load(this.userImageURL)
                    .override(300, 300)
                    .centerInside()
                    .circleCrop()
                    .into(it.ivStory)
                it.root.setOnClickListener {
                    listener.onStoryClicked(this)
                }
            }
        }
    }
}