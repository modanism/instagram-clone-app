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
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_DATE = "extra_date"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val  = if (Build.VERSION.SDK_INT >= 33) {
//            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
//        } else {
//            @Suppress("DEPRECATION")
//            intent.getParcelableExtra(EXTRA_STORY)
//        }

        val name = intent.getStringExtra(EXTRA_USERNAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val photoUrl = intent.getStringExtra(EXTRA_IMAGE)
        val createdAt = intent.getStringExtra(EXTRA_DATE)





            val caption = "${name} ${description}"
            val startIndex = caption.indexOf(name!!)
            val endIndex = startIndex + name.length

            val spannableString = SpannableString(caption)
            spannableString.setSpan(StyleSpan(1), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.apply {
                Glide.with(this@DetailActivity)
                    .load(photoUrl)
                    .centerCrop()
                    .into(ivDetail)
                tvDetailDate.text = formatDate(createdAt!!)
                tvDetailDesc.text = spannableString
                tvUsername.text = name
                actionBack.setOnClickListener {
                    finishAfterTransition()
                }
            }


    }

    private fun formatDate(date : String) : String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        val inputDate = inputDateFormat.parse(date)
        val outputDate = outputDateFormat.format(inputDate!!)
        return outputDate.uppercase()
    }


}