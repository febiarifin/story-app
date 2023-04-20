package com.febiarifin.storyappsubmissiondicoding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febiarifin.storyappsubmissiondicoding.data.model.Story
import com.febiarifin.storyappsubmissiondicoding.databinding.ItemStoryBinding

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private val listStories = ArrayList<Story>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=  onItemClickCallback
    }

    fun setList(stories: ArrayList<Story>){
        listStories.clear()
        listStories.addAll(stories)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(story: Story){
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(story)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = story.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position])
    }

    override fun getItemCount(): Int = listStories.count()

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }
}