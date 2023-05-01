package com.example.instagramclone

import android.content.Intent
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale


class StoryAdapter(private val listStory : List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_story, viewGroup, false))

    override fun getItemCount() = listStory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (photoUrl, createdAt, name, description, id) = listStory[position]

        holder.tvAuthorItem.text = name
        holder.tvDateItem.text = formatDate(createdAt)
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .centerCrop()
            .into(holder.ivItem)

        holder.ivItem.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context,DetailActivity::class.java)
            intentDetail.putExtra("extra_story", listStory[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val ivItem : ImageView = view.findViewById(R.id.img_item_photo)
        val tvAuthorItem : TextView = view.findViewById(R.id.tv_item_author)
        val tvDateItem : TextView = view.findViewById(R.id.tv_item_date)
    }


    private fun formatDate(postedDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = dateFormat.parse(postedDate)
        val time = date?.time

        val now = System.currentTimeMillis()

        val duration = time?.let { DateUtils.getRelativeTimeSpanString(it, now, DateUtils.SECOND_IN_MILLIS) }

        return "Posted $duration"
    }

}