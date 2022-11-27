package com.example.narayanagrouptask.repositories

import androidx.lifecycle.MutableLiveData
import com.example.narayanagrouptask.models.Owner
import com.example.narayanagrouptask.network.ApiInterface
import com.example.narayanagrouptask.utils.BASE_URL
import com.example.narayanagrouptask.utils.ProgressVisibility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val apiInterface: ApiInterface) {
    val loadingVisibility: MutableLiveData<ProgressVisibility> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()
    val successMsg: MutableLiveData<String> = MutableLiveData()
    val detailsList: MutableLiveData<List<Owner>> = MutableLiveData()

    fun getDetailsRepos(mCompositDisposible: CompositeDisposable, htmlUrl: String) {
        var endPoint=htmlUrl.replace(BASE_URL,"").replace("repos/","")
        var items = endPoint.split("/")

        mCompositDisposible.add(
            apiInterface.getRepoDetails(items.get(0), items.get(1))
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
        response: List<Owner>?
    ) {
        loadingVisibility.value = ProgressVisibility.GONE
        //Incomplete result returns false if there's data(Status)
        if (response!! != null && response.size > 0) {
            detailsList.value = response
            //saveUsersInDb(mCompositeDisposable = mCompositDisposible,users = items)
        }
    }

    private fun updateFailure(error: Throwable) {
        loadingVisibility.value = ProgressVisibility.GONE
        errorMsg.value = error.localizedMessage
    }
}