package com.hasbrain.areyouandroiddev;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Simon Pham on 4/16/18.
 * Email: simonpham.dn@gmail.com
 */
public class PostViewActivity extends AppCompatActivity {

    private WebView vWeb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

        initialize();

        String url = getIntent().getStringExtra("PostListActivity.POST_URL");
        loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initialize() {
        vWeb = findViewById(R.id.webView);

        // enable javascript
        vWeb.setWebViewClient(new WebViewClient());
        vWeb.getSettings().setJavaScriptEnabled(true);
    }

    private void loadUrl(String url) {
        if (vWeb != null) vWeb.loadUrl(url);
    }
}
