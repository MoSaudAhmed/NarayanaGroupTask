package com.example.narayanagrouptask.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.narayanagrouptask.models.HomeAllRepositoriesResponse
import com.example.narayanagrouptask.repositories.HomeRepository
import com.example.narayanagrouptask.utils.ProgressVisibility
import com.example.narayanagrouptask.utils.checkInternet
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    application: Application, private val homeRepository: HomeRepository
) : AndroidViewModel(application) {

    var loadingVisibility: MutableLiveData<ProgressVisibility> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    val internetStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    var searchReposResponse: MutableLiveData<HomeAllRepositoriesResponse> = MutableLiveData()
    var items = homeRepository.getAllRepos().cachedIn(viewModelScope)
    var successMsg: MutableLiveData<String> = MutableLiveData()

    init {
        loadingVisibility = homeRepository.loadingVisibility
        errorMessage = homeRepository.errorMsg
        searchReposResponse = homeRepository.searchReposResponse
        successMsg = homeRepository.successMsg

    }

    fun fetchSearchRepos(str: String) {
        if (checkInternet(getApplication<Application>().applicationContext)) {
            internetStatus.postValue(true)
            homeRepository.getSearchRepos(mCompositeDisposable, str)
        } else {
            internetStatus.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }

    fun validateAndInsertDataToDB() {
        //("Not yet implemented")
    }
}