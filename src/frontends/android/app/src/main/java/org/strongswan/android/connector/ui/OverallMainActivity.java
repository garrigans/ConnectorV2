package org.strongswan.android.connector.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.strongswan.android.R;
import org.strongswan.android.ui.MainActivity;

/* Main activity to encompass Connector functionality and the StrongSwan L2TP IPSEC solution. The
   app is constructed in this way as to not interfere with StrongSwans build cycles.
 */

public class OverallMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_main);
    }

    // Launch the VPN product.
    public void launchQuickVpnTest(View v){
        System.out.println("Test trace activity");
        Intent intent = new Intent(OverallMainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // Launch the Connector diagnostics product.
    public void launchDiagnosticTools(View v){
        System.out.println("Test trace activity");
        Intent intent = new Intent(OverallMainActivity.this, MainActivityCon.class);
        startActivity(intent);
    }
}
