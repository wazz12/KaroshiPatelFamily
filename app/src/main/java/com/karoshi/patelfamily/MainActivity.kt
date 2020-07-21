package com.karoshi.patelfamily

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.karoshi.patelfamily.feature.appToolbarTitle
import com.karoshi.patelfamily.utils.visibleIfTrue
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_toolbar.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolBar(getString(R.string.app_name))
        setWebView()
    }

    private fun setToolBar(title: String) {
        toolbarNavIcon.visibleIfTrue(false)
        toolbar.appToolbarTitle(title)
    }

    private fun setWebView() {
        val webSetting = webView.settings
        webSetting.javaScriptEnabled = true
        webSetting.useWideViewPort = true
        webSetting.loadWithOverviewMode = true
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.displayZoomControls = false
        webView.webViewClient = WebViewClient()
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.loadUrl("file:///android_asset/karoshi_patel_family_tree.html")
    }
}