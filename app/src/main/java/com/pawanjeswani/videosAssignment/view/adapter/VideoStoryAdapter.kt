package com.pawanjeswani.videosAssignment.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pawanjeswani.videosAssignment.databinding.ItemVideoStoryBinding
import com.pawanjeswani.videosAssignment.model.Hit


class VideoStoryAdapter (private val listener:StoryClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var storyList = mutableListOf<Hit>()

    fun submitList(list: MutableList<Hit>) {
        storyList = list
        notifyItemRangeChanged(0, storyList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoStoryViewHolder(
            ItemVideoStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return storyList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoStoryViewHolder).bindTo(field = storyList[position],listener)
    }

}

interface StoryClickListener{
    fun onStoryClicked(hit: Hit)
}