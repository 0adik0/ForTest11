package ru.rybak

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import ru.rybak.ui.theme.X10AcademwebviewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppsflyerManager.init(this)
        setContent { MainScreen(this) }
    }

    fun initSubIds(sub1: String?, sub2: String?, sub3: String?, sub4: String?, sub5: String?) {
        AppsflyerManager.initWithSubIds(this, sub1, sub2, sub3, sub4, sub5)
    }
}

@Composable
fun MainScreen(mainActivity: MainActivity) {
    X10AcademwebviewTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {},
                content = { CustomWebView(mainActivity); it }
            )
        }
    }
}

@Composable
fun CustomWebView(mainActivity: MainActivity) {

    val sharedPrefs = mainActivity.getSharedPreferences("AppsflyerData", Context.MODE_PRIVATE)

    val sub1 = sharedPrefs.getString("sub1", "None")!!
    val sub2 = sharedPrefs.getString("sub2", "None")!!
    val sub3 = sharedPrefs.getString("sub3", "None")!!
    val sub4 = sharedPrefs.getString("sub4", "None")!!
    val sub5 = sharedPrefs.getString("sub5", "None")!!

    val neededUrl =
        "https://xn--80adgdap9a0aiak.xn--p1ai/?keyword=exampleKeyword&external_id=exampleID&sub1=$sub1&sub2=$sub2&sub3=$sub3&sub4=$sub4&sub5=$sub5&app_id=exampleAppId&af_id=exampleAfId&advertising_id=exampleAdId&appsflyer_uid=exampleAfUid"

    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                        Log.d("CustomWebView", "Loading URL: ${request.url}")
                        mainActivity.initSubIds(sub1, sub2, sub3, sub4, sub5)
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(neededUrl)
            }
        },
        update = {
            Log.d("CustomWebView", "Updating URL: $neededUrl")
            it.loadUrl(neededUrl)
        },
    )
}