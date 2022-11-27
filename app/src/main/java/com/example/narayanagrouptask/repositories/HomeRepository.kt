package com.example.narayanagrouptask.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.narayanagrouptask.models.HomeAllRepositoriesResponse
import com.example.narayanagrouptask.network.ApiInterface
import com.example.narayanagrouptask.ui.paging.RepositoryPagingSource
import com.example.narayanagrouptask.utils.ProgressVisibility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) {
    val loadingVisibility: MutableLiveData<ProgressVisibility> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()
    val successMsg: MutableLiveData<String> = MutableLiveData()
    val allUsersResponse: MutableLiveData<HomeAllRepositoriesResponse> = MutableLiveData()

    fun getAllRepos() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 50),
        pagingSourceFactory = { RepositoryPagingSource(apiInterface,"search/repositories?q=page=") }
    ).liveData

/*    fun getAllRepos(mCompositDisposible: CompositeDisposable, pageNumber: Int, pageLimit: Int) {
        mCompositDisposible.add(
            apiInterface.getAllRepositories("search/repositories?q=page=" + 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = ProgressVisibility.VISIBLE }
                .subscribe(
                    { response -> updateResponse(mCompositDisposible, response) },
                    { error -> updateFailure(error) }
                ))
    }*/

    fun getSearchRepos(mCompositDisposible: CompositeDisposable, str: String) {
        mCompositDisposible.add(
            apiInterface.getSearchRepositories(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = ProgressVisibility.VISIBLE }
                .subscribe(
                    { response -> updateResponse(mCompositDisposible, response) },
                    { error -> updateFailure(error) }
                ))
    }

    private fun updateResponse(
        mCompositDisposible: CompositeDisposable,
        response: HomeAllRepositoriesResponse?
    ) {
        loadingVisibility.value = ProgressVisibility.GONE
        //Incomplete result returns false if there's data(Status)
        if (!response!!.status!! && response!!.repoList != null) {
            allUsersResponse.value = response
            //saveUsersInDb(mCompositeDisposable = mCompositDisposible,users = items)
        }
    }

    private fun updateFailure(error: Throwable) {
        loadingVisibility.value = ProgressVisibility.GONE
        errorMsg.value = error.localizedMessage
    }

}