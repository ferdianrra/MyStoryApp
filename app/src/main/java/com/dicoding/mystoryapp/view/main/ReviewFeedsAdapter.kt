package com.dicoding.mystoryapp.view.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mystoryapp.network.response.ListStoryItem
import com.dicoding.mystoryapp.databinding.FeedsReviewBinding
import com.dicoding.mystoryapp.formatDate

class ReviewFeedsAdapter : PagingDataAdapter<ListStoryItem, ReviewFeedsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback =onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    class MyViewHolder (val binding: FeedsReviewBinding ): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListStoryItem) {
            binding.tvItemName.text = review.name
            binding.date.text = formatDate(review.createdAt)
            binding.descFeeds.text = review.description
            Glide.with(binding.root)
                .load(review.photoUrl)
                .into(binding.ivItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FeedsReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review =getItem(position)
        if (review != null) {
            holder.bind(review)
        }
        holder.itemView.setOnClickListener { review?.let { it1 ->
            onItemClickCallback.onItemClicked(
                it1
            )
        } }
    }

}