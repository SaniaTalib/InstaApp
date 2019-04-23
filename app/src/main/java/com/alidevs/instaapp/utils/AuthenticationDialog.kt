import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alidevs.instaapp.utils.AuthenticationListener

class AuthenticationDialog(context: Context, private val listener: AuthenticationListener) : Dialog(context) {
    private val request_url: String=""
    private val redirect_url: String=""

   // internal var webViewClient: WebViewClient = object : WebViewClient() {

    /*    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(redirect_url)) {
                this@AuthenticationDialog.dismiss()
                return true
            }
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            if (url.contains("access_token=")) {
                val uri = Uri.EMPTY.parse(url)
                var access_token = uri.getEncodedFragment()
                access_token = access_token!!.substring(access_token!!.lastIndexOf("=") + 1)
                Log.e("access_token", access_token)
                listener.onTokenReceived(access_token!!)
                dismiss()
            } else if (url.contains("?error")) {
                Log.e("access_token", "getting error fetching access token")
                dismiss()
            }
        }
    }*/

    /*init {
        this.redirect_url = context.resources.getString(R.string.redirect_url)
        this.request_url = context.resources.getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.resources.getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=token&display=touch&scope=public_content"
    }*/

/*    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        *//*this.setContentView(R.layout.auth_dialog)
        initializeWebView()*//*
    }*/

  /*  private fun initializeWebView() {
        val webView = findViewById(R.id.webView)
        webView.getSettings().setJavaScriptEnabled(true)
        webView.loadUrl(request_url)
        webView.setWebViewClient(webViewClient)
    }*/
}