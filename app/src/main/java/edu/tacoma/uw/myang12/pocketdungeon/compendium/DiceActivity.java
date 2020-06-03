package edu.tacoma.uw.myang12.pocketdungeon.compendium;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.tacoma.uw.myang12.pocketdungeon.R;

public class DiceActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        webView = findViewById(R.id.dice_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getString(R.string.dice));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
