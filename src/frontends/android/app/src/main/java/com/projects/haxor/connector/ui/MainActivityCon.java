package com.projects.haxor.connector.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.strongswan.android.R;


public class MainActivityCon extends AppCompatActivity {

    private Button buttonTestRoute;
    private Button buttonTestVpnConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

//        findViewById(android.R.id.content).setBackgroundColor();
//        this.buttonTestRoute = (Button) this.findViewById(R.id.buttonTestRoute);
//        this.buttonTestVpnConnection = (Button) this.findViewById(R.id.buttonTestVpnConnection);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                // User chose the "Settings" item, show the app settings UI...
//                return true;
//
//            case R.id.action_favorite:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    public void launchTraceActivity(View v){
        System.out.println("Test trace activity");
        Intent intent = new Intent(MainActivityCon.this, TraceActivity.class);
        startActivity(intent);
    }

//    public void launchTestVpnActivity(View v){
//        System.out.println("Test VPN");
//        Intent intent = new Intent(MainActivityCon.this, TestVpnActivity.class);
//        startActivity(intent);
//    }

    public void launchLookupIpActivity(View v){
        System.out.println("Lookup Ip details");
        Intent intent = new Intent(MainActivityCon.this, IPLookupActivity.class);
        startActivity(intent);

    }

    public void launchHelpActivity(View v){
        System.out.println("Help");
        Intent intent = new Intent(MainActivityCon.this, HelpActivity.class);
        startActivity(intent);
    }

    public void launchTestPortActivity(View v){
        System.out.println("Test port activity");
        Intent intent = new Intent(MainActivityCon.this, TestPortAccess.class);
        startActivity(intent);
    }
}
