/**
 * @author haxor on 4/11/16.
 */

package org.strongswan.android.connector.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BaseBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
