package co.paystack.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.paystack.android.R;
import co.paystack.android.api.di.ApiModule;

public class AuthActivity extends Activity {
    private static final String TAG = "AuthActivity";
    final AuthSingleton si = AuthSingleton.getInstance();
    private WebView webView;
    private String responseJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_paystack_android____activity_auth);
        setTitle("Authorize your card");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        webView = findViewById(R.id.webView);
        setup();
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

    protected void setup() {
        setContentView(R.layout.co_paystack_android____activity_auth);

        findViewById(R.id.iv_close).setOnClickListener(v -> finish());

        webView = findViewById(R.id.webView);
        webView.setKeepScreenOn(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                WebMessageHelper.init(view);
                if (url.contains(ApiModule.API_URL + "close")) {
                    handleResponse();
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });

        webView.loadUrl(si.getUrl());
    }

    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.stopLoading();
        }
        handleResponse();
    }
}
