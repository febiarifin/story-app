package com.febiarifin.storyappsubmissiondicoding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem
import com.febiarifin.storyappsubmissiondicoding.databinding.ItemStoryBinding

class StoryAdapter(
    private val onItemClicked: (id: String?, name: String?, createdAt: String?) -> Unit
) : PagingDataAdapter<StoryResponseItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryResponseItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(data.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = data.name

                itemView.setOnClickListener {
                    onItemClicked(data?.id, data?.name, data?.createdAt)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponseItem>() {
            override fun areItemsTheSame(
                oldItem: StoryResponseItem,
                newItem: StoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryResponseItem,
                newItem: StoryResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}