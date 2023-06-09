package com.example.instagramclone.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.R
import com.example.instagramclone.activities.DetailActivity
import com.example.instagramclone.getElapsedTimeSinceDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class StoryAdapter(private val listStory : List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(viewGroup.context).inflate(
        R.layout.item_row_story, viewGroup, false))

    override fun getItemCount() = listStory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (photoUrl, createdAt, name, description, id, lat, lon) = listStory[position]
        holder.tvAuthorItem.text = name
        holder.tvDateItem.text = getElapsedTimeSinceDate(createdAt)
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .centerCrop()
            .into(holder.ivItem)

        holder.ivItem.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("extra_story", listStory[holder.adapterPosition])

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.ivItem,"image"),
                )

            holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
        }

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val ivItem : ImageView = view.findViewById(R.id.img_item_photo)
        val tvAuthorItem : TextView = view.findViewById(R.id.tv_item_author)
        val tvDateItem : TextView = view.findViewById(R.id.tv_item_date)
    }



}