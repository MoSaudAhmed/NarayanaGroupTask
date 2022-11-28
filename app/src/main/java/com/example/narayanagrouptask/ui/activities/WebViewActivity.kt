package com.example.narayanagrouptask.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.narayanagrouptask.R

lateinit var progressBar: ProgressBar

class WebViewActivity : AppCompatActivity() {

    var url = ""
    var name = ""

    lateinit var tvProfileNm: TextView
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        initViews()

        webView.webViewClient = myWebClient()
        webView.settings.javaScriptEnabled = true

        url = intent.getStringExtra("url")!!
        name = intent.getStringExtra("name")!!
        tvProfileNm.text = name

        webView.loadUrl(url)
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        tvProfileNm = findViewById(R.id.tvProfileNm)
        webView = findViewById(R.id.webView)
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }
}

class myWebClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        progressBar!!.visibility = View.VISIBLE
        view.loadUrl(url)
        return true
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        progressBar!!.visibility = View.GONE
    }
}