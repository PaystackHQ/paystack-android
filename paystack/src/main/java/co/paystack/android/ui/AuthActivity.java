package co.paystack.android.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.paystack.android.R;
import co.paystack.android.api.ApiClient;

public class AuthActivity extends Activity {

    final AuthSingleton si = AuthSingleton.getInstance();
    private WebView webView;
    private String responseJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_paystack_android____activity_auth);
        setTitle("Authorize your card");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        webView = (WebView) findViewById(R.id.webView);
    }

    public void handleResponse() {
        if (responseJson == null) {
            responseJson = "{\"status\":\"requery\",\"message\":\"Reaffirm Transaction Status on Server\"}";
        }
        synchronized (si) {
            si.setResponseJson(responseJson);
            si.notify();
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.co_paystack_android____activity_auth);

        webView = (WebView) findViewById(R.id.webView);
        webView.setKeepScreenOn(true);

        abstract class AuthResponseJI {
            @SuppressWarnings("unused")
            public abstract void processContent(String aContent);
        }

        class AuthResponseLegacyJI extends AuthResponseJI {
            public void processContent(String aContent) {
                responseJson = aContent;
                handleResponse();
            }
        }

        class AuthResponse17JI extends AuthResponseJI {

            @JavascriptInterface
            public void processContent(String aContent) {
                responseJson = aContent;
                handleResponse();
            }
        }

        class JIFactory {

            private AuthResponseJI getJI() {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return new AuthResponse17JI();
                } else {
                    return new AuthResponseLegacyJI();
                }

            }
        }


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new JIFactory().getJI(), "INTERFACE");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains(ApiClient.API_URL + "charge/three_d_response/")) {
                    view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementById('return').innerText);");
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });

        webView.loadUrl(si.getUrl());
    }

    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.stopLoading();
            webView.removeJavascriptInterface("INTERFACE");
        }
        handleResponse();
    }

}
