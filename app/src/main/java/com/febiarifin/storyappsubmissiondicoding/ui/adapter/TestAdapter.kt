package com.febiarifin.storyappsubmissiondicoding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febiarifin.storyappsubmissiondicoding.data.model.Story
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem
import com.febiarifin.storyappsubmissiondicoding.databinding.ItemStoryBinding

//class TestAdapter (
//    private val onItemClicked: (id: String?, ivStory: ImageView?, tvName: TextView?) -> Unit
//) : PagingDataAdapter<Story, TestAdapter.ViewHolder>(DIFF_CALLBACK)  {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val story = getItem(position)
//        holder.bind(story)
//    }
//
//    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(story: Story?) {
//            with(binding) {
//                Glide.with(itemView.context)
//                    .load(story?.photoUrl)
//                    .into(ivItemPhoto)
//                tvItemName.text = story?.name
//
//                itemView.setOnClickListener {
//                    onItemClicked(story?.id, ivItemPhoto, tvItemName)
//                }
//            }
//        }
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
//            override fun areItemsTheSame(oldItem: Story, newItem: Story) =
//                oldItem.id == newItem.id
//
//            override fun areContentsTheSame(oldItem: Story, newItem: Story) =
//                oldItem == newItem
//        }
//    }
//
//}

class TestAdapter :
    PagingDataAdapter<StoryResponseItem, TestAdapter.MyViewHolder>(DIFF_CALLBACK) {

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

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryResponseItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(data.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = data.name
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponseItem>() {
            override fun areItemsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}