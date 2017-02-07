/**
 * @author haxor on 4/11/16.
 */

package org.strongswan.android.connector.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import org.strongswan.android.R;
import org.strongswan.android.connector.utils.BaseBrowser;


public class HelpActivity extends AppCompatActivity {

    private WebView wv1;

    // Create a web view to hold the help html page. The page is loaded from the anddroid raw
    // resources directory.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help2);

        // Create the web view.
        wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new BaseBrowser());

        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // Load the html file from raw resource directory.
        wv1.loadUrl("file:///android_res/raw/helpdoc.html");

    }

}
