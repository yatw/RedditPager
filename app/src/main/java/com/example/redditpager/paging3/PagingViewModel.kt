package com.example.redditpager.paging3


import androidx.lifecycle.*
import androidx.paging.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.redditpager.models.EnvelopedSubmission
import com.example.redditpager.models.Submission
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class PagerViewModel(
    private val repository: PagingRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val pagingDataFlow: Flow<PagingData<ListItem>>

    init {

        val searchFlow = MutableSharedFlow<String>()
        val searches = searchFlow
            .distinctUntilChanged()
            .onStart { emit("MostBeautiful") }

        pagingDataFlow = searches
            .flatMapConcat {
                getSubmission(subReddit = it)
            }
            .cachedIn(viewModelScope)

    }


    private fun getSubmission(subReddit: String): Flow<PagingData<ListItem>> =
        repository.getSubmissions(subReddit)
            .map { pagingData: PagingData<EnvelopedSubmission> -> pagingData.map {
                val sub = it.submission
                    ListItem.Sub(sub.title, sub.url, sub.ups)
                }
            }


    sealed class ListItem {
        data class Sub(val title: String, val url: String?, val ups: Int) : ListItem()
        data class Separator(val upvote: String) : ListItem()
    }

    class Factory(
        owner: SavedStateRegistryOwner,
        private val repository: PagingRepository
    ) : AbstractSavedStateViewModelFactory(owner, null) {

        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            if (modelClass.isAssignableFrom(PagerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PagerViewModel(repository, handle) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}