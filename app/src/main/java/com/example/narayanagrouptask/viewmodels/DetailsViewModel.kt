package com.example.narayanagrouptask.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.narayanagrouptask.models.Owner
import com.example.narayanagrouptask.repositories.DetailsRepository
import com.example.narayanagrouptask.utils.ProgressVisibility
import com.example.narayanagrouptask.utils.checkInternet
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    application: Application, private val detailsRepository: DetailsRepository
) : AndroidViewModel(application) {
    var loadingVisibility: MutableLiveData<ProgressVisibility> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    val internetStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    var detailsList: MutableLiveData<List<Owner>> = MutableLiveData()
    var successMsg: MutableLiveData<String> = MutableLiveData()


    init {
        loadingVisibility = detailsRepository.loadingVisibility
        errorMessage = detailsRepository.errorMsg
        detailsList = detailsRepository.detailsList
        successMsg = detailsRepository.successMsg

    }

    fun fetchDetailsRepo(htmlUrl: String) {
        if (checkInternet(getApplication<Application>().applicationContext)) {
            //internetStatus.value = true
            detailsRepository.getDetailsRepos(mCompositeDisposable,htmlUrl)

        } else {
            //internetStatus.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}