package com.dicoding.mystoryapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.mystoryapp.databinding.ActivityDetailFeedsBinding
import com.dicoding.mystoryapp.formatDate

class DetailFeedsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFeedsBinding
    private val detailFeedsViewModel by viewModels<DetailFeedsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFeedsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("ID").toString()
        val token = intent.getStringExtra("TOKEN").toString()
        showDetailFeeds(id, token)

        detailFeedsViewModel.detailFeeds.observe(this) {
            binding.date.text = formatDate(it.createdAt)
            binding.tvDetailDescription.text = it.description
            binding.tvDetailName.text = it.name
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.ivDetailPhoto)

        }

        detailFeedsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.AppBarHome.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

    }

    private fun showLoading(isloading: Boolean) {
        binding.progressBar.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    private fun showDetailFeeds(id : String, token: String) {
        detailFeedsViewModel.showDetailFeeds(id, token)
    }


}