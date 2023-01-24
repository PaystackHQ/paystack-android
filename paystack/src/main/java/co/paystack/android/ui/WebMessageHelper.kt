package co.paystack.android.ui

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebMessagePortCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import androidx.webkit.WebViewFeature.POST_WEB_MESSAGE
import androidx.webkit.WebViewFeature.WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK

object WebMessageHelper {
    private const val TAG = "WebMessageHelper"
    @JvmStatic
    fun init(webView: WebView) {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.CREATE_WEB_MESSAGE_CHANNEL)) {
            val (receiver, sender) = WebViewCompat.createWebMessageChannel(webView)
            if (WebViewFeature.isFeatureSupported(WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK)) {
                Log.i(TAG,  "web messages supported")

                receiver.setWebMessageCallback(object :
                    WebMessagePortCompat.WebMessageCallbackCompat() {
                    override fun onMessage(port: WebMessagePortCompat, message: WebMessageCompat?) {
                        val dataStr = message?.data ?: return
                        Log.i(TAG, dataStr)
                    }
                })

                if (WebViewFeature.isFeatureSupported(POST_WEB_MESSAGE)) {
                    val message = WebMessageCompat("""{"type": "init_port"}""", arrayOf(sender))
                    WebViewCompat.postWebMessage(webView, message, Uri.EMPTY)
                } else {
                    Log.e(TAG,  "No post Web message support")
                }
            } else {
                Log.e(TAG,  "No post Web message support")

            }
        } else {
            Log.e(TAG,  "No Web message port set message support")
        }
    }
}