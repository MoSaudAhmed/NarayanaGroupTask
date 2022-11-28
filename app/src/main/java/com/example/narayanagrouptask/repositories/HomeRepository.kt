package com.example.narayanagrouptask.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.narayanagrouptask.dao.DBHelper
import com.example.narayanagrouptask.models.HomeAllRepositoriesResponse
import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.network.ApiInterface
import com.example.narayanagrouptask.ui.paging.RepositoryPagingSource
import com.example.narayanagrouptask.utils.ProgressVisibility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val dbHelper: DBHelper
) {
    val loadingVisibility: MutableLiveData<ProgressVisibility> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()
    val successMsg: MutableLiveData<String> = MutableLiveData()
    val searchReposResponse: MutableLiveData<HomeAllRepositoriesResponse> = MutableLiveData()
    val allReposResponse: MutableLiveData<List<RepoItem>> = MutableLiveData()


    fun getAllRepos() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 50),
        pagingSourceFactory = {
            RepositoryPagingSource(
                apiInterface,
                "search/repositories?q=page=", allReposResponse
            )
        }
    ).liveData

    fun checkNeedToSyncDB(mCompositeDisposable: CompositeDisposable, repossList: List<RepoItem>?) {
        mCompositeDisposable.add(
            dbHelper.fetchNeedToSyncCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response < 15) saveReposInDb(
                            mCompositeDisposable,
                            repossList
                        )
                    },
                    { error ->
                        saveReposInDb(
                            mCompositeDisposable,
                            repossList
                        )
                    })
        )
    }

    fun saveReposInDb(
        mCompositeDisposable: CompositeDisposable,
        repossList: List<RepoItem>?
    ) {
        if (repossList != null) {
            mCompositeDisposable.add(
                dbHelper.saveReposInDb(repossList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { response -> updateSaveInDbResponse(response) },
                        { error -> updateFailure(error) })
            )
        }
    }

    fun fetchReposFromDb(mCompositeDisposable: CompositeDisposable) {

        mCompositeDisposable.add(dbHelper.fetchRepos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingVisibility.value = ProgressVisibility.VISIBLE }
            .subscribe(
                { response -> updateFetchReposFromDbResponse(response) },
                { error -> updateFailure(error) })
        )
    }

    private fun updateFetchReposFromDbResponse(response: List<RepoItem>) {
        loadingVisibility.value = ProgressVisibility.GONE
        var homemodel = HomeAllRepositoriesResponse(-1, false, response)
        homemodel!!.repoList = response
        searchReposResponse.value = homemodel
    }

    private fun updateSaveInDbResponse(response: Int) {
        loadingVisibility.value = ProgressVisibility.GONE
    }

    fun getSearchRepos(mCompositDisposible: CompositeDisposable, str: String) {
        mCompositDisposible.add(
            apiInterface.getSearchRepositories(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.postValue(ProgressVisibility.VISIBLE) }
                .subscribe(
                    { response -> updateResponse(response) },
                    { error -> updateFailure(error) }
                ))
    }

    private fun updateResponse(
        response: HomeAllRepositoriesResponse?
    ) {
        loadingVisibility.value = ProgressVisibility.GONE
        if (!response!!.status!! && response!!.repoList != null) {
            searchReposResponse.value = response
        }
    }

    private fun updateFailure(error: Throwable) {
        loadingVisibility.value = ProgressVisibility.GONE
        errorMsg.value = error.localizedMessage
    }

}