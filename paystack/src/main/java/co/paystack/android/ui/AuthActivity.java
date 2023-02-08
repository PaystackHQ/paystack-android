package co.paystack.android.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;

import co.paystack.android.BuildConfig;
import co.paystack.android.R;
import co.paystack.android.api.di.ApiModule;

public class AuthActivity extends Activity {

    final AuthSingleton si = AuthSingleton.getInstance();
    private WebView webView;
    private String responseJson;
    private static final String TAG = "AuthActivity";

    private Pusher pusher;
    private Channel channel;
    private String channelName = "3DS_" + si.getTransactionId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_paystack_android____activity_auth);
        setTitle("Authorize your card");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        webView = findViewById(R.id.webView);
        setupWebview();

        listenForChargeEvents();
    }

    private void listenForChargeEvents() {
        PusherOptions options = new PusherOptions();
        options.setCluster("eu");

        pusher = new Pusher(BuildConfig.PUSHER_KEY, options);
        pusher.connect();

        channel = pusher.subscribe(channelName);

        channel.bind("pusher:subscription_succeeded", event -> Log.d(TAG, event.toString()));
        channel.bind("pusher:subscription_error", event -> Log.e(TAG, "Pusher subscription error: " + event.toString()));
        channel.bind("response", event -> {
            Log.i(TAG, event.toString());
            responseJson = event.getData();
            handleResponse();
        });
    }

    public void handleResponse() {
        if (responseJson == null) {
            responseJson = "{\"status\":\"requery\",\"message\":\"Reaffirm Transaction Status on Server\"}";
        }
        synchronized (si) {
            si.setResponseJson(responseJson);
            si.notifyAll();
        }
        finish();
    }

    protected void setupWebview() {
        setContentView(R.layout.co_paystack_android____activity_auth);

        findViewById(R.id.iv_close).setOnClickListener(v -> {
            synchronized (si) {
                si.notify();
            }
            finish();
        });

        webView = findViewById(R.id.webView);
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
                if (url.contains(ApiModule.LEGACY_API_URL + "charge/three_d_response/")) {
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

    public void onDestroy() {
        pusher.disconnect();
        super.onDestroy();
        if (webView != null) {
            webView.stopLoading();
            webView.removeJavascriptInterface("INTERFACE");
        }
    }
}
