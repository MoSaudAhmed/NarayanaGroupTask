package com.example.narayanagrouptask.dao

import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.utils.RESULT_ERROR
import com.example.narayanagrouptask.utils.RESULT_SUCCESS
import io.reactivex.Single
import javax.inject.Inject


class DBHelper @Inject constructor(private val ngtDao: NGTDao?) {

    fun provideDao(): NGTDao? {
        return ngtDao
    }

    fun saveReposInDb(residents: List<RepoItem>): Single<Int> {

        return Single.create { emitter ->
            try {

                val ids: LongArray? = ngtDao?.insertReposInDB(residents)
                if (ids == null) {
                    emitter.onSuccess(RESULT_ERROR)
                } else {
                    emitter.onSuccess(RESULT_SUCCESS)
                }

            } catch (e: java.lang.Exception) {
                emitter.onError(e)
            }
        }
    }

    fun fetchRepos(): Single<List<RepoItem>> {
        return Single.create { emitter ->
            try {

                val residents: List<RepoItem>? = ngtDao?.getRepos()
                if (residents != null) {
                    emitter.onSuccess(residents)
                } else {
                    emitter.onSuccess(arrayListOf())
                }

            } catch (e: java.lang.Exception) {
                emitter.onError(e)
            }
        }
    }

    fun fetchNeedToSyncCount(): Single<Int> {
        return Single.create { emitter ->
            try {

                val needToSync:Int? = ngtDao?.getReposCount()
                if (needToSync != null ) {
                    emitter.onSuccess(needToSync)
                } else {
                    emitter.onSuccess(0)
                }

            } catch (e: java.lang.Exception) {
                emitter.onError(e)
            }
        }
    }
}