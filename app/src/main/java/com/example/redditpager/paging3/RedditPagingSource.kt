package com.example.redditpager.paging3

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.redditpager.api.SubmissionClient
import com.example.redditpager.models.EnvelopedSubmission
import com.example.redditpager.models.EnvelopedSubmissionListing
import com.example.redditpager.models.SubmissionListing
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


class RedditPagingSource(
    private val subReddit: String,
    private val submissionClient: SubmissionClient
) : PagingSource<String, EnvelopedSubmission>() {


    data class Node(
        val value: String,
        var prev: Node? = null,
        var next: Node? = null
    )

    private val keyMap = mutableMapOf<String, Node>()



    /**
     * A refresh happens whenever the Paging library wants to load new data to replace the current list,
     *
     * e.g., on swipe to refresh or on invalidation due to database updates,
     * config changes, process death, etc.
     *
     * Typically, subsequent refresh calls will want to restart loading data centered around PagingState.anchorPosition which represents the most recently accessed index.
     *
     * If return null, paging library will return user to the top of the list.
     *
     * https://stackoverflow.com/a/69649194/5777189
     */
    override fun getRefreshKey(state: PagingState<String, EnvelopedSubmission>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let {
                it.data
            }
            null
        }
    }

    /**
     * The load() function will be called by the Paging library to asynchronously fetch more data to be displayed as the user scrolls around.
     * The LoadParams object keeps information related to the load operation, including the following:
     *
     * Key of the page to be loaded. If this is the first time that load is called, LoadParams.key will be null. In this case, you will have to define the initial page key.
     *
     * Load size - the requested number of items to load.
     */
    override suspend fun load(params: LoadParams<String>): LoadResult<String, EnvelopedSubmission> {

        val page = params.key
        return try {

            Timber.i("Fetching from PagingSource...")

            val response: EnvelopedSubmissionListing =
                submissionClient.getSubmission(
                    subReddit,
                    if (page == "") 15 else params.loadSize,
                    null,
                    page)

            val data: SubmissionListing = response.data
            Timber.i("Fetched ${data.envelopedSubmissions.size} items")


            val currentPage = if (page != null) keyMap[page] else {

                // using "" to represent the initial load, it is needed when you scroll back back up
                val first = Node(value = "", prev = null)
                keyMap[""] = first
                first
            }

            // create next page node
            data.after?.let {
                val n = Node(value = it, prev = currentPage)
                currentPage?.next = n
                keyMap[it] = n
            }

            LoadResult.Page(
                data = data.envelopedSubmissions,
                prevKey = currentPage?.prev?.value,
                nextKey = data.after
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }




}