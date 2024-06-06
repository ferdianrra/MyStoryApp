package com.dicoding.mystoryapp

import com.dicoding.mystoryapp.network.response.ListStoryItem

object DataDummy {

    fun generateDummyFeedsResponse(): List<ListStoryItem> {
        val items : MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val feeds = ListStoryItem(
                i.toString(),
                "photoUrl + $i",
                "createdAt +  $i",
                "name +  $i",
                "description +  $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(feeds)
        }
        return items
    }
}