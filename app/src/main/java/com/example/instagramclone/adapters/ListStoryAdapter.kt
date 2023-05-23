package com.example.instagramclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.databinding.ItemRowStoryBinding
import com.example.instagramclone.getElapsedTimeSinceDate


class ListStoryAdapter :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {

    var onItemClick: ((View, ListStoryItem) -> Unit)? = null

    inner class ListViewHolder(
        var binding: ItemRowStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.rootView.setOnClickListener { itemRootView ->
                val story = getItem(bindingAdapterPosition)
                story?.let {
                    onItemClick?.invoke(itemRootView, it)
                }
            }
        }

        fun bind(data: ListStoryItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(data.photoUrl)
                    .into(imgItemPhoto)

                tvItemAuthor.text = data.name
                tvItemDate.text = getElapsedTimeSinceDate(data.createdAt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}