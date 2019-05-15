package com.alidevs.instaapp.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.alidevs.instaapp.App.Companion.context
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_watches.*

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var url: String = ""
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var user_id: String
    val networkAvailability : Boolean
        get() = Utils.isNetworkAvailable(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)


        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        actionBar?.elevation = 4.0F
        actionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        webView = findViewById(R.id.webView1)

        if (networkAvailability){
            firebaseAuth = FirebaseAuth.getInstance()
            user_id = firebaseAuth.currentUser!!.uid
            firestore = FirebaseFirestore.getInstance()
        }

        val bundle = intent.extras
        if (bundle != null) {
            url = bundle.getString("url")!!
            startWebView(url)
        } else {
            onBackPressed()
        }
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
                Toast.makeText(this@NewsDetailActivity, "Error:$description", Toast.LENGTH_SHORT).show()
            }
        }
        webView.loadUrl(url)
    }

    override fun onStart() {
        super.onStart()
        if (networkAvailability){
            val currentUser = firebaseAuth.currentUser?.uid
            if (currentUser == null) {
                sendToLogin()
            }
        }else{
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    private fun sendToLogin() {
        val intent = Intent(this@NewsDetailActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}