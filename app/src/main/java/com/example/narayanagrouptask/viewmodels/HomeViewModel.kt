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
    //var allReposResponse: MutableLiveData<HomeAllRepositoriesResponse> = MutableLiveData()
    var items=homeRepository.getAllRepos().cachedIn(viewModelScope)
    var successMsg: MutableLiveData<String> = MutableLiveData()
    var pageNumber: Int = 1
    var pageLimit: Int = 10
    var isLoading: Boolean = true

    init {
        loadingVisibility = homeRepository.loadingVisibility
        errorMessage = homeRepository.errorMsg
      //  allReposResponse = homeRepository.allUsersResponse
        successMsg = homeRepository.successMsg

    }

    fun fetchRepos() {
        if (checkInternet(getApplication<Application>().applicationContext)) {
            //internetStatus.value = true
            //homeRepository.getAllRepos(mCompositeDisposable,pageNumber,pageLimit)
            val list = homeRepository.getAllRepos().cachedIn(viewModelScope)

        } else {
            //internetStatus.value = false
            /*if (messagesRequest != null) {
                messagesRepo.fetchMessageFromDb(
                    mCompositeDisposable,
                    msgLinkId,
                    messagesRequest.PageNumber,
                    20
                )
            } else if (familyMessagesRequest != null) {
                messagesRepo.fetchMessageFromDb(
                    mCompositeDisposable,
                    msgLinkId,
                    familyMessagesRequest.PageNumber,
                    20
                )
            }*/
        }
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}