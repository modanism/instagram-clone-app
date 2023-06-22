package com.example.instagramclone.models

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.adapters.ListStoriesAdapter
import com.example.instagramclone.paging.StoriesRepository
import com.example.instagramclone.utils.DataDummy
import com.example.instagramclone.utils.MainDispatcherRule
import com.example.instagramclone.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PagingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()


    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Mock
    private lateinit var application: Application

    @Test
    fun `when Get List Story Should Not Null and Return Data`() = runTest {
        val dataDummy = DataDummy.generateDummyStoriesEntity()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dataDummy)
        val expectedData = MutableLiveData<PagingData<ListStoryItem>>()
        expectedData.value = data
        Mockito.`when`(storiesRepository.getStories("Bearer 123456")).thenReturn(expectedData)
        val pagingViewModel = PagingViewModel(application, storiesRepository, "Bearer 123456")
        val actualData: PagingData<ListStoryItem> = pagingViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(ListStoriesAdapter.DIFF_CALLBACK,noopListUpdateCallback,Dispatchers.Main)
        differ.submitData(actualData)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataDummy.size, differ.snapshot().size)
        Assert.assertEquals(dataDummy[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get List Story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedData = MutableLiveData<PagingData<ListStoryItem>>()
        expectedData.value = data
        Mockito.`when`(storiesRepository.getStories("Bearer 123456")).thenReturn(expectedData)
        val pagingViewModel = PagingViewModel(application, storiesRepository, "Bearer 123456")
        val actualData: PagingData<ListStoryItem> = pagingViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualData)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {

    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
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