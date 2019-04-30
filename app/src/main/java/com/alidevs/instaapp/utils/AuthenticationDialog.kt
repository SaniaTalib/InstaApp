package com.alidevs.instaapp.utils

import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

class AuthenticationDialog : Dialog{
    private val listener: AuthenticationListener? = null

    constructor(context: Context, listener: AuthenticationListener) : super(context)

    private val request_url: String = "https://api.instagram.com/oauth/authorize/?client_id=ccfeba403b9543e2b6fff98956cbdc6d&redirect_uri=https://instagram.com/&response_type=token&display=touch&scope=public_content"
    private val redirect_url: String = "https://instagram.com/"

    private lateinit var webViewClient: WebViewClient
/*
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog)
        initializeWebView()
    }

    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(request_url)
        webView.webViewClient = webViewClient
    }*/
}