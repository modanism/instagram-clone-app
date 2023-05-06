package com.example.instagramclone.activities

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val storyData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORY)
        }


        if (storyData != null) {

            val caption = "${storyData.name} ${storyData.description}"
            // Find the index of the start and end of the word you want to make bold
            val startIndex = caption.indexOf(storyData.name)
            val endIndex = startIndex + storyData.name.length

            // Create a SpannableString and set a StyleSpan to the bold word
            val spannableString = SpannableString(caption)
            spannableString.setSpan(StyleSpan(1), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.apply {
                Glide.with(this@DetailActivity)
                    .load(storyData.photoUrl)
                    .centerCrop()
                    .into(ivDetail)
                tvDetailDate.text = formatDate(storyData.createdAt)
                tvDetailDesc.text = spannableString
                tvUsername.text = storyData.name
                actionBack.setOnClickListener {
                    finishAfterTransition()
                }
            }
        }

    }

    private fun formatDate(date : String) : String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        val inputDate = inputDateFormat.parse(date)
        val outputDate = outputDateFormat.format(inputDate)
        return outputDate.uppercase()
    }


}