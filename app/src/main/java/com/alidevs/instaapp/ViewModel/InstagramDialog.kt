package com.alidevs.instaapp.ViewModel

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Layout
import android.util.Log
import android.view.Display
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.view.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.log
import android.webkit.WebSettings
import com.alidevs.instaapp.R


class InstagramDialog(context: Context?, listner: AuthenticationListner) : Dialog(context) {

    private var listner = listner
    private var URL = null
    private var cont: Context? = context

    private var CLIENT_ID = "8f3804004212478dbcdebd7e0c977647"
    private var Client_Secret = "9bd94e1966d94625a84bf8c539d8a30f"

    private var webview: WebView? = null;
    //    private var TOKEN_URL = "https://api.instagram.com/oauth/access_token"
    private var AUTH_URL = "https://instagram.com/oauth/authorize/"

    private var Base_URL = "https://instagram.com/"

    private var Redirect_URL = "http://www.instagram.com"

    private val FILL:FrameLayout.LayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT-15,ViewGroup.LayoutParams.FILL_PARENT-35)

    private val MarginVertical:Int = 50
    private val MarginHorizonral:Int = 15

    private val httpAuth = AUTH_URL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + Redirect_URL +
            "&response_type=token&scope=public_content"

    private var conn = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog)


        initWebview()


    }

    fun initWebview() {
        webview = findViewById(R.id.insta_webview)

        val settings = webview!!.settings
        settings.javaScriptEnabled = true
        webview!!.scrollBarStyle = WebView.SCROLLBARS_INSIDE_OVERLAY


        var display:Display = window?.windowManager!!.defaultDisplay

        webview!!.webViewClient = object : WebViewClient() {
            var accetoken: String = ""
            var authcomplete: Boolean = false

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)


                if (url!!.contains("#access_token=") && !authcomplete) {
                    var uri: Uri = Uri.parse(url)
                    accetoken = uri.encodedFragment
                    accetoken = accetoken.substring(accetoken.lastIndexOf("=") + 1)
                    authcomplete = true

                    listner.OnCodeRecieved(accetoken)
                    dismiss()

                } else if (url.contains("?error")) {
                    Log.e("erorr", url)
                    dismiss()
                }

            }

        }
        webview!!.loadUrl(httpAuth)


    }
}