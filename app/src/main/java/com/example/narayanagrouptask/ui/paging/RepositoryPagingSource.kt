package com.example.narayanagrouptask.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.network.ApiInterface
import com.example.narayanagrouptask.utils.HOME_REPO_LIMIT
import javax.inject.Inject

class RepositoryPagingSource constructor(
    private val apiInterface: ApiInterface,
    private val str: String
) :
    PagingSource<Int, RepoItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoItem> {
        return try {
            val position = params.key ?: 1
            val response = apiInterface.getAllRepositories(str + position, HOME_REPO_LIMIT)

            return LoadResult.Page(
                data = response.repoList,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.total_items) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepoItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}