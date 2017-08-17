package com.flybits.webapptester.activities;

import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flybits.webapptester.R;
import com.flybits.webapptester.adapters.CallbackMessageAdapter;
import com.flybits.webapptester.models.JSMessage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String urlEntry;

    //UI
    Toolbar toolbar;
    ProgressBar progressBar;
    WebView webWebView;
    TextView txtNumMessages;
    RecyclerView lstMessages;

    //Other
    ArrayList<JSMessage> messages = new ArrayList<>();
    CallbackMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        urlEntry = getIntent().getStringExtra("url");

        if (!urlEntry.startsWith("http://") && !urlEntry.startsWith("https://"))
            urlEntry = "http://" + urlEntry;

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.loadProgressBar);

        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);

        webWebView = findViewById(R.id.activity_main_webWebview);
        txtNumMessages = findViewById(R.id.activity_main_txtMessageCount);
        lstMessages = findViewById(R.id.activity_main_lstMessages);

        txtNumMessages.setText(getResources().getString(R.string.label_nummessages, 0));

        lstMessages.setHasFixedSize(false);

        adapter = new CallbackMessageAdapter(this, messages);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        lstMessages.setLayoutManager(manager);
        lstMessages.setAdapter(adapter);

        progressBar.setMax(100);

        webWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                toolbar.setTitle(String.format("Loading (%d%%)", progress));
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100) {
                    toolbar.setTitle(R.string.app_name);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webWebView.setWebViewClient(new WebViewClient());

        WebSettings settings = webWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webWebView.addJavascriptInterface(this, "android");

        webWebView.loadUrl(urlEntry);

    }

    @JavascriptInterface
    public void parseWebAppMessage(final String json)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSMessage message = new JSMessage(SystemClock.elapsedRealtime()/1000, json);
                messages.add(0, message);
                adapter.notifyDataSetChanged();
                txtNumMessages.setText(getResources().getString(R.string.label_nummessages, messages.size()));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_main_refresh:
                webWebView.loadUrl(urlEntry);
                messages.clear();
                txtNumMessages.setText(getResources().getString(R.string.label_nummessages, messages.size()));
                adapter.notifyDataSetChanged();
                return true;
            case R.id.menu_main_test_androidtojs:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webWebView.evaluateJavascript("parseNativeAppMessage('{\"message\":\"Hello from Android!\"}');", null);
                } else {
                    webWebView.loadUrl("javascript:parseNativeAppMessage('{\"message\":\"Hello from Android!\"}');");
                }
                return true;
        }
        return false;
    }
}
