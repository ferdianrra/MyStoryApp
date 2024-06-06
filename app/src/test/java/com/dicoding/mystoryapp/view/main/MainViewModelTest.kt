package com.dicoding.mystoryapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.mystoryapp.DataDummy
import com.dicoding.mystoryapp.MainDispatcherRule
import com.dicoding.mystoryapp.data.FeedsRepository
import com.dicoding.mystoryapp.getOrAwaitValue
import com.dicoding.mystoryapp.network.UserRepository
import com.dicoding.mystoryapp.network.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var feedsRepository: FeedsRepository
    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `When get feed should not null and return data`() = runTest {
        val dummyFeeds = DataDummy.generateDummyFeedsResponse()
        val data: PagingData<ListStoryItem>  = FeedsPagingSource.snapshot(dummyFeeds)
        val expectedFeeds = MutableLiveData<PagingData<ListStoryItem>>()
        expectedFeeds.value = data
        Mockito.`when`(feedsRepository.getFeeds()).thenReturn(expectedFeeds)

        val mainViewModel = MainViewModel(userRepository ,feedsRepository)
        val actualFeeds: PagingData<ListStoryItem> = mainViewModel.feedsPhoto.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ReviewFeedsAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualFeeds)

        // memeriksa ketersediaan dan jumlah data dari adapter.
        assertNotNull(differ.snapshot())
        assertEquals(dummyFeeds.size, differ.snapshot().size)
        assertEquals(dummyFeeds[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Feeds Empty Should Return No Data`() = runTest {
        val data : PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedFeeds = MutableLiveData<PagingData<ListStoryItem>>()
        expectedFeeds.value = data
        Mockito.`when`(feedsRepository.getFeeds()).thenReturn(expectedFeeds)

        val mainViewModel = MainViewModel(userRepository, feedsRepository)
        val actualFeeds : PagingData<ListStoryItem> = mainViewModel.feedsPhoto.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ReviewFeedsAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualFeeds)

        assertEquals(0, differ.snapshot().size)
    }
}

class FeedsPagingSource: PagingSource<Int, LiveData<List<ListStoryItem>>>() {

    companion object {
        fun snapshot(item: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(item)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}

}