package com.example.narayanagrouptask.ui.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.narayanagrouptask.R
import com.example.narayanagrouptask.di.ViewModelFactory
import com.example.narayanagrouptask.models.Owner
import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.ui.adapters.ContributorsAdapter
import com.example.narayanagrouptask.viewmodels.DetailsViewModel
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class RepoDetailedActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var contributorsAdapter: ContributorsAdapter? = null
    lateinit var tv_name: TextView
    lateinit var tv_description: TextView
    lateinit var tv_repoUrl: TextView
    lateinit var img_avatar: ImageView
    lateinit var rv_contributors: RecyclerView

    private lateinit var detailsViewModel: DetailsViewModel

    companion object {
        const val TAG = "RepoDetailedActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detailed)

        initViews()

        //Could improve.
        var item =
            Gson().fromJson<RepoItem>(intent.getStringExtra("PassingData"), RepoItem::class.java)

        tv_name.text = "${item.name}"
        tv_description.text = "${item.description}"

        Glide.with(this).load(item.owner!!.avatarUrl).circleCrop()
            .error(R.drawable.ic_baseline_person_48).into(img_avatar)

        tv_repoUrl.text = "${item.htmlUrl}"
        tv_repoUrl.setPaintFlags(tv_repoUrl.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        tv_repoUrl.setOnClickListener {
            var intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", tv_repoUrl.text.toString())
            startActivity(intent)
        }

        setupRecyclerView()

        detailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)

        detailsViewModel.fetchDetailsRepo(item.contributorsUrl!!)

        detailsViewModel.detailsList.observe(this, { response ->
            updateRecyclerItems(response)
        })


    }

    private fun updateRecyclerItems(response: List<Owner>?) {
        contributorsAdapter!!.updateList(response!!)
    }

    private fun setupRecyclerView() {
        contributorsAdapter = ContributorsAdapter(this, ArrayList())
        rv_contributors.setHasFixedSize(true)
        rv_contributors.layoutManager = LinearLayoutManager(this)
        rv_contributors.adapter = contributorsAdapter
        rv_contributors.setNestedScrollingEnabled(false)
    }

    private fun initViews() {
        tv_name = findViewById(R.id.tv_name)
        tv_description = findViewById(R.id.tv_description)
        tv_repoUrl = findViewById(R.id.tv_repoUrl)
        img_avatar = findViewById(R.id.img_avatar)
        rv_contributors = findViewById(R.id.rv_contributors)
    }
}