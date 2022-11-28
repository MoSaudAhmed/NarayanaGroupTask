package com.example.narayanagrouptask.ui.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.narayanagrouptask.R
import com.example.narayanagrouptask.di.ViewModelFactory
import com.example.narayanagrouptask.models.Owner
import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.ui.adapters.ContributorsAdapter
import com.example.narayanagrouptask.ui.callbacks.ContributorsClickListener
import com.example.narayanagrouptask.utils.ProgressVisibility
import com.example.narayanagrouptask.viewmodels.DetailsViewModel
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailsActivity : DaggerAppCompatActivity(), ContributorsClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var contributorsAdapter: ContributorsAdapter? = null
    lateinit var tv_name: TextView
    lateinit var tv_description: TextView
    lateinit var tv_repoUrl: TextView
    lateinit var tvProfileNm: TextView
    lateinit var img_avatar: ImageView
    lateinit var rv_contributors: RecyclerView
    lateinit var progress_detailsActivity: ProgressBar

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var intentItem: RepoItem
    private var contributorsList: ArrayList<Owner> = ArrayList()

    companion object {
        const val TAG = "DetailsActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detailed)

        initViews()

        setupIntentData()

        setupRecyclerView()

        detailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)

        detailsViewModel.fetchDetailsRepo(intentItem.contributorsUrl!!)

        detailsViewModel.detailsList.observe(this, { response ->
            contributorsList = response as ArrayList<Owner>
            updateRecyclerItems()
        })

        detailsViewModel.loadingVisibility.observe(this, Observer { progress ->
            if (progress == ProgressVisibility.VISIBLE) progress_detailsActivity.visibility =
                View.VISIBLE else progress_detailsActivity.visibility = View.GONE
        })

        detailsViewModel.internetStatus.observe(this, Observer { connectionStatus ->
            if (!connectionStatus) {
                Toast.makeText(
                    ChangePasswordActivity@ this,
                    resources.getString(R.string.NoInternetConnection),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        detailsViewModel.errorMessage.observe(this, Observer { text ->
            Toast.makeText(ChangePasswordActivity@ this, text, Toast.LENGTH_LONG).show()
        })

    }

    private fun setupIntentData() {
        intentItem =
            Gson().fromJson<RepoItem>(intent.getStringExtra("PassingData"), RepoItem::class.java)

        tvProfileNm.text = "${intentItem.name}"
        tv_name.text = "${intentItem.fullName}"
        tv_description.text = "${intentItem.description}"

        Glide.with(this).load(intentItem.owner!!.avatarUrl).circleCrop()
            .error(R.drawable.ic_baseline_person_48).into(img_avatar)

        tv_repoUrl.text = "${intentItem.htmlUrl}"
        tv_repoUrl.setPaintFlags(tv_repoUrl.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        tv_repoUrl.setOnClickListener {
            startWebViewActivity(tv_repoUrl.text.toString(), tvProfileNm.text.toString())
        }
    }

    private fun updateRecyclerItems() {
        contributorsAdapter!!.updateList(contributorsList)
    }

    private fun setupRecyclerView() {
        contributorsAdapter = ContributorsAdapter(this, contributorsList, this)
        rv_contributors.setHasFixedSize(true)
        rv_contributors.layoutManager = LinearLayoutManager(this)
        rv_contributors.adapter = contributorsAdapter
        rv_contributors.setNestedScrollingEnabled(false)
    }

    private fun initViews() {
        tvProfileNm = findViewById(R.id.tvProfileNm)
        tv_name = findViewById(R.id.tv_name)
        tv_description = findViewById(R.id.tv_description)
        tv_repoUrl = findViewById(R.id.tv_repoUrl)
        img_avatar = findViewById(R.id.img_avatar)
        rv_contributors = findViewById(R.id.rv_contributors)
        progress_detailsActivity = findViewById(R.id.progress_detailsActivity)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

    }

    override fun onItemClick(position: Int) {
        startWebViewActivity(
            contributorsList.get(position).htmlUrl!!, contributorsList.get(position).login!!
        )
    }

    private fun startWebViewActivity(url: String, name: String) {
        var intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("name", name)
        startActivity(intent)
    }
}