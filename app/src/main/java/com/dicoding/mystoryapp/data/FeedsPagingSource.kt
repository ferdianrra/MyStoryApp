package com.dicoding.mystoryapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.mystoryapp.network.response.ListStoryItem
import com.dicoding.mystoryapp.network.retrofit.ApiService
import java.lang.Error

class FeedsPagingSource (private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val TAG = "FeedsPagingSource"
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage =  state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStory(position, params.loadSize)
            val stories = responseData.listStory

            LoadResult.Page(
                data = stories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (stories.isNullOrEmpty()) null else position+1
            )
        } catch (exception: Exception) {
            Log.e(TAG, Error(exception).toString())
            return LoadResult.Error(exception)
        }
    }
}