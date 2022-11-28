package com.example.narayanagrouptask.dao

import androidx.room.TypeConverter
import com.example.narayanagrouptask.models.License
import com.example.narayanagrouptask.models.Owner
import com.example.narayanagrouptask.models.RepoItem
import com.google.gson.Gson


class HomeRepoListConverters {

    @TypeConverter
    fun listToJson(value: ArrayList<RepoItem>?) = Gson().toJson(value ?: arrayListOf<RepoItem>())

    @TypeConverter
    fun jsonToList(value: String?) = if (value != null) {
        Gson().fromJson(value, Array<RepoItem>::class.java).toList()
    } else {
        arrayListOf()
    }
}

class OwnerListConverters {

    @TypeConverter
    fun listToJson(value: ArrayList<Owner>?) = Gson().toJson(value ?: arrayListOf<Owner>())

    @TypeConverter
    fun jsonToList(value: String?) = if (value != null) {
        Gson().fromJson(value, Array<Owner>::class.java).toList()
    } else {
        arrayListOf()
    }
}
class LicenseListConverters {

    @TypeConverter
    fun listToJson(value: ArrayList<License>?) = Gson().toJson(value ?: arrayListOf<License>())

    @TypeConverter
    fun jsonToList(value: String?) = if (value != null) {
        Gson().fromJson(value, Array<License>::class.java).toList()
    } else {
        arrayListOf()
    }
}
class StringConverters {

    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value?: arrayListOf<String>())

    @TypeConverter
    fun jsonToList(value: String?) = if(value!=null){ Gson().fromJson(value, Array<String>::class.java).toList()}else{
        arrayListOf()
    }
}