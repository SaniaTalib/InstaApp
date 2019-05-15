package com.alidevs.instaapp.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.alidevs.instaapp.App
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_add_watches.*

class TermsConditionsActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    val networkAvailability : Boolean
        get() = Utils.isNetworkAvailable(App.context!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_conditions)

        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        actionBar?.elevation = 4.0F
        actionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        webView = findViewById(R.id.webView1)

        startWebView("https://tempusapp.co/privacy_policy.html")
    }

    private fun startWebView(url: String) {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY

        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(this@TermsConditionsActivity, "Error:$description", Toast.LENGTH_SHORT).show()
            }
        }
        webView.loadUrl(url)
    }
}
