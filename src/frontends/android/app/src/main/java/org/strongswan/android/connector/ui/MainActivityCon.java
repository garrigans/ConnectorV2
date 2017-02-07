/**
 * @author haxor on 4/11/16.
 */

package org.strongswan.android.connector.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.strongswan.android.R;
import org.strongswan.android.ui.MainActivity;

/* Main activity for the Connector diagnostics app. It is seperated from the StrongSwan VPN solution.

 */
public class MainActivityCon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    public void launchTraceActivity(View v){
        Log.d(MainActivity.class.getSimpleName(), "Launcing trace route activity");
        Intent intent = new Intent(MainActivityCon.this, TraceActivity.class);
        startActivity(intent);
    }

    public void launchTestVpnActivity(View v){
        Log.d(MainActivity.class.getSimpleName(), "Launcing VPN test activity");
        Intent intent = new Intent(MainActivityCon.this, MainActivity.class);
        startActivity(intent);
    }

    public void launchLookupIpActivity(View v){
        Log.d(MainActivity.class.getSimpleName(), "Launcing  IP lookup activity");
        Intent intent = new Intent(MainActivityCon.this, IPLookupActivity.class);
        startActivity(intent);

    }

    public void launchHelpActivity(View v){
        Log.d(MainActivity.class.getSimpleName(), "Launcing help activity");
        Intent intent = new Intent(MainActivityCon.this, HelpActivity.class);
        startActivity(intent);
    }

    public void launchTestPortActivity(View v){
        Log.d(MainActivity.class.getSimpleName(), "Launcing port test activity");
        Intent intent = new Intent(MainActivityCon.this, TestPortAccess.class);
        startActivity(intent);
    }
}
