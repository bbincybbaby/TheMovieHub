@file:OptIn(ExperimentalCoroutinesApi::class)

package me.bincy.movielist.ui.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.bincy.movielist.R
import me.bincy.movielist.data.MovieRepositoryImpl
import me.bincy.movielist.data.models.Movie
import org.junit.After
import org.junit.Before
import org.junit.Test


class MovieListViewModelTest {

    private val savedInstanceStateHandle: SavedStateHandle = SavedStateHandle()
    private lateinit var movieListViewModel: MovieListViewModel
    private val realMoshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val context: Context = mockk(relaxed = true)
    private val repository: MovieRepositoryImpl = MovieRepositoryImpl(realMoshi,context)

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        movieListViewModel = MovieListViewModel(
            savedStateHandle = savedInstanceStateHandle,
            movieRepository = repository
        )
    }

    @Test
    fun keyBoardVisibilityTrue() {
        movieListViewModel.setKeyboardVisibility(true)
        assertEquals(movieListViewModel.isKeyBoardVisible(), true)
    }

    @Test
    fun keyBoardVisibilityFalse() {
        movieListViewModel.setKeyboardVisibility(false)
        assertEquals(movieListViewModel.isKeyBoardVisible(), false)
    }

    @Test
    fun queryFlowReturnTheValueProvided1() {
        movieListViewModel.setQuery("family")
        assertEquals("family",movieListViewModel.queryStateFlow.value)
    }

    @Test
    fun queryFlowReturnTheValueProvided2() {
        movieListViewModel.setQuery("life")
        assertNotSame("family",movieListViewModel.queryStateFlow.value)
    }

    @Test
    fun movieListTest() = runTest(UnconfinedTestDispatcher()){
        val file1 =  javaClass.classLoader?.getResourceAsStream("content_page_1.json")
        every {
            context.resources.openRawResource(R.raw.content_page_1)
        } returns file1!!
        val items: Flow<PagingData<Movie>> = movieListViewModel.moviesList
        movieListViewModel.setQuery("")
        val snapshot: List<Movie> = items.asSnapshot {
            scrollTo(20)
        }
        assertEquals(20,snapshot.size)
    }

    @Test
    fun movieListScrolledTest() = runTest(UnconfinedTestDispatcher()){
        val file1 =  javaClass.classLoader?.getResourceAsStream("content_page_1.json")
        every {
            context.resources.openRawResource(R.raw.content_page_1)
        } returns file1!!
        val file2 =  javaClass.classLoader?.getResourceAsStream("content_page_2.json")
        every {
            context.resources.openRawResource(R.raw.content_page_2)
        } returns file2!!
        val file3 =  javaClass.classLoader?.getResourceAsStream("content_page_3.json")
        every {
            context.resources.openRawResource(R.raw.content_page_3)
        } returns file3!!
        val items: Flow<PagingData<Movie>> = movieListViewModel.moviesList
        movieListViewModel.setQuery("")
        val snapshot: List<Movie> = items.asSnapshot {
            scrollTo(40)
        }
        assertEquals(54,snapshot.size)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}