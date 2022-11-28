package com.example.narayanagrouptask.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.narayanagrouptask.R
import com.example.narayanagrouptask.di.ViewModelFactory
import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.ui.adapters.HomeRepositoryAdapter
import com.example.narayanagrouptask.ui.adapters.RepositoryPagingAdapter
import com.example.narayanagrouptask.ui.callbacks.HomeRepoCLickListener
import com.example.narayanagrouptask.utils.ProgressVisibility
import com.example.narayanagrouptask.viewmodels.HomeViewModel
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeActivity : DaggerAppCompatActivity(), HomeRepoCLickListener {

    companion object {
        const val TAG = "HomeActivity"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var materialSearchView: SearchView
    private lateinit var rv_home_repos: RecyclerView
    private lateinit var pagingAdapter: RepositoryPagingAdapter
    private lateinit var progress_home: ProgressBar
    private lateinit var swipe_to_refresh: SwipeRefreshLayout

    var searchAdapter: HomeRepositoryAdapter? = null

    var searchList: ArrayList<RepoItem> = ArrayList()
    var allRepoList: ArrayList<RepoItem> = ArrayList()

    //Timer to load data on text change in searchView
    var timer = Timer()
    val DELAY: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)

        initViews()

        pagingAdapter = RepositoryPagingAdapter(this, this)
        searchAdapter = HomeRepositoryAdapter(this, searchList, this)
        setupPagingRecyclerView()

        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        homeViewModel.items.observe(this, Observer {
            pagingAdapter.submitData(lifecycle, it)
        })

        homeViewModel.allReposResponse.observe(this, Observer {
            allRepoList.addAll(it)
            homeViewModel.validateAndInsertDataToDB(allRepoList)
        })
        homeViewModel.searchReposResponse.observe(this, Observer {
            searchList = it.repoList as ArrayList<RepoItem>
            updateRecyclerView()
        })
        homeViewModel.loadingVisibility.observe(this, Observer { progress ->
            if (progress == ProgressVisibility.VISIBLE) progress_home.visibility =
                VISIBLE else progress_home.visibility = GONE
        })
        homeViewModel.internetStatus.observe(this, Observer { connectionStatus ->
            if (!connectionStatus) {
                Toast.makeText(
                    ChangePasswordActivity@ this,
                    resources.getString(R.string.NoInternetConnection),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        homeViewModel.errorMessage.observe(this, Observer { text ->
            Toast.makeText(ChangePasswordActivity@ this, text, Toast.LENGTH_LONG).show()
        })

        materialSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i(TAG, "onQueryTextSubmit: " + query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Added delay to avoid interruption while entering data in Search
                initiateSearchData(newText)
                return true
            }
        })
    }

    private fun initiateSearchData(newText: String?) {
        if (newText!!.length > 0) {
            Log.i(TAG, "onQueryTextChange: " + newText)
            timer.cancel()
            timer = Timer()
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        homeViewModel.fetchSearchRepos(newText!!)
                    }
                },
                DELAY
            )
        } else {
            setupPagingRecyclerView()
        }
    }

    private fun initViews() {
        rv_home_repos = findViewById(R.id.rv_home_repos)
        swipe_to_refresh = findViewById(R.id.swipe_to_refresh)
        progress_home = findViewById(R.id.progress_home)
        materialSearchView = findViewById(R.id.materialSearchView)
        materialSearchView.visibility = VISIBLE
        findViewById<ImageView?>(R.id.ivBack).visibility = GONE
        findViewById<TextView>(R.id.tvProfileNm).visibility = GONE

        swipe_to_refresh.setOnRefreshListener {
            swipe_to_refresh.isRefreshing = false
            homeViewModel.fetchRepoData()
            materialSearchView.setQuery("", false)
            materialSearchView.clearFocus()
        }
    }

    private fun setupPagingRecyclerView() {
        rv_home_repos.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = pagingAdapter
        }
    }

    private fun updateRecyclerView() {
        rv_home_repos.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = searchAdapter
        }
        searchAdapter!!.updateList(searchList)
    }

    override fun onItemClicked(position: Int) {
        var intent = Intent(this, DetailsActivity::class.java)
        /*There are many ways of passing data:
        1. passing string and converting it back to object using GSON
        2. Parcelable
        3. Serializable*/
        intent.putExtra(
            "PassingData",
            Gson().toJson(
                if (rv_home_repos.adapter == pagingAdapter) pagingAdapter.snapshot().items.get(
                    position
                ) else searchList.get(position)
            )
        )
        startActivity(intent)
    }
}