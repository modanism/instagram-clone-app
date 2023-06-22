package com.example.instagramclone.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.R
import com.example.instagramclone.activities.DetailActivity
import com.example.instagramclone.databinding.ItemRowStoryBinding
import com.example.instagramclone.getElapsedTimeSinceDate

class ListStoriesAdapter :
    PagingDataAdapter<ListStoryItem, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    inner class ListViewHolder(private var binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Log.d("MainActivty", "bind: ${story}")
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(story)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgItemPhoto)
                tvItemAuthor.text = story.name
                tvItemDate.text = getElapsedTimeSinceDate(story.createdAt)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        Log.d("Adapter", "onCreateViewHolder: CREATED")
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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

//class ListStoriesAdapter : PagingDataAdapter<ListStoryItem, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding =
//            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//
//        return ListViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val data = getItem(position)
//        if (data != null) {
//            holder.bind(data)
//        }
//    }
//
//    inner class ListViewHolder(
//        var binding: ItemRowStoryBinding
//    ) : RecyclerView.ViewHolder(binding.root) {
//
////        init {
////            itemView.rootView.setOnClickListener { itemRootView ->
////                val story = getItem(bindingAdapterPosition)
////                story?.let {
////                    onItemClick?.invoke(itemRootView, it)
////                }
////            }
////        }
//
//        fun bind(data: ListStoryItem) {
//            Log.d(TAG, data.toString())
//            binding.apply {
//                Glide.with(itemView)
//                    .load(data.photoUrl)
//                    .into(imgItemPhoto)
//
//                tvItemAuthor.text = data.name
//                tvItemDate.text = getElapsedTimeSinceDate(data.createdAt)
//            }
//        }
//    }
//
//    companion object {
//
//        const val TAG = "Adapter"
//
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
//            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(
//                oldItem: ListStoryItem,
//                newItem: ListStoryItem
//            ): Boolean {
//                return oldItem.id == newItem.id
//            }
//        }
//    }
//}