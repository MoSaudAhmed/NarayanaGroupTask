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

    lateinit var ivBack: ImageView
    lateinit var tvProfileNm: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        progressBar = findViewById(R.id.progressBar)
        val webView = findViewById<WebView>(R.id.webView)

        webView.webViewClient = myWebClient()
        webView.settings.javaScriptEnabled = true

        url = intent.getStringExtra("url")!!

        webView.loadUrl(url)
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