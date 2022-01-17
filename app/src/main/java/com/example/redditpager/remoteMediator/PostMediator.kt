package com.example.redditpager.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.redditpager.api.SubmissionClient
import com.example.redditpager.db.PagerDatabase
import com.example.redditpager.db.Post
import com.example.redditpager.db.PostKeys
import com.example.redditpager.models.EnvelopedSubmission
import com.example.redditpager.models.EnvelopedSubmissionListing
import com.example.redditpager.models.SubmissionListing
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


@ExperimentalPagingApi
class PostMediator(
    private val subReddit: String,
    private val client: SubmissionClient,
    private val database: PagerDatabase
): RemoteMediator<Int, Post>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Post>): MediatorResult {

        val page: String? = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            Timber.i("fetch items from mediator...")
            val result: EnvelopedSubmissionListing = client.getSubmission(subReddit, state.config.pageSize, null, page)
            val submissionListing: SubmissionListing = result.data
            val submissions: List<EnvelopedSubmission> = submissionListing.envelopedSubmissions
            Timber.i("fetched ${submissions.size} submissions")

            val nextKey = submissionListing.after
            val endOfPaginationReached = nextKey == null
            database.withTransaction {

                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.postDao().clearPosts()
                    database.postKeysDao().clearRemoteKeys()
                }

                val postEntityList = submissions.map {
                    val sub = it.submission
                    val post = Post(title = sub.title, url = sub.url, ups = sub.ups)
                    val id = database.postDao().insert(post)

                    // save the prev key
                    val keys = PostKeys(postId = id, prevKey = page, nextKey = nextKey)
                    database.postKeysDao().insert(keys)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Post>): PostKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.postKeysDao().remoteKeysId(repoId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Post>): PostKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item

        return state.pages.firstOrNull{
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { it ->
            database.postKeysDao().remoteKeysId(it.id)
        }

        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                database.postKeysDao().remoteKeysId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Post>): PostKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                // Get the remote keys of the last item retrieved
                database.postKeysDao().remoteKeysId(it.id)
            }
    }

}