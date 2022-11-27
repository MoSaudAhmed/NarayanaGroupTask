package com.example.narayanagrouptask.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.narayanagrouptask.R
import com.example.narayanagrouptask.di.ViewModelFactory
import com.example.narayanagrouptask.models.HomeAllRepositoriesResponse
import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.ui.adapters.HomeRepositoryAdapter
import com.example.narayanagrouptask.ui.adapters.RepositoryPagingAdapter
import com.example.narayanagrouptask.ui.callbacks.HomeRepoCLickListener
import com.example.narayanagrouptask.viewmodels.HomeViewModel
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), HomeRepoCLickListener {

    companion object {
        const val TAG = "HomeActivity";
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var ivBack: ImageView
    private lateinit var tvProfileNm: TextView
    private lateinit var materialSearchView: SearchView
    private lateinit var rv_home_repos: RecyclerView
    private lateinit var homeAdapter: RepositoryPagingAdapter
    var allRepoList: ArrayList<RepoItem> = ArrayList()
    var homeRepositoryAdapter: HomeRepositoryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)

        initViews()
        homeAdapter = RepositoryPagingAdapter(this, this)
        setupRecyclerView()


        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        /* homeViewModel.fetchRepos()
         homeViewModel.allUsersResponse.observe(this, Observer { reposResponse ->
             updateRecyclerView(reposResponse.repoList)
         })*/
        homeViewModel.items.observe(this, Observer {
            homeAdapter.submitData(lifecycle, it)
        })

    }

    private fun initViews() {
        rv_home_repos = findViewById(R.id.rv_home_repos)
        ivBack = findViewById(R.id.ivBack)
        materialSearchView = findViewById(R.id.materialSearchView)
        materialSearchView.visibility = VISIBLE
        tvProfileNm = findViewById(R.id.tvProfileNm)
        tvProfileNm.visibility = GONE
        ivBack.visibility = GONE

    }

    private fun setupRecyclerView() {
        //homeRepositoryAdapter = HomeRepositoryAdapter(this, allRepoList, this)
        rv_home_repos.setHasFixedSize(true)
        rv_home_repos.layoutManager = LinearLayoutManager(this)
//        rv_home_repos.adapter = homeRepositoryAdapter
        rv_home_repos.adapter = homeAdapter
    }

    private fun updateRecyclerView(newRepoList: List<RepoItem>) {
        homeRepositoryAdapter!!.updateList(newRepoList)
    }

    override fun onItemClicked(position: Int) {
        var intent = Intent(this, RepoDetailedActivity::class.java)
        //There are many ways of passing data:
        //1. passing string and converting it back to object using GSON
        //2. Parcelable
        //3. Serializable
        intent.putExtra("PassingData", Gson().toJson(homeAdapter.snapshot().items.get(position)))
        startActivity(intent)
    }
}