package org.strongswan.android.connector.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.strongswan.android.R;
import org.strongswan.android.ui.MainActivity;

public class OverallMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_main);
    }

    public void launchQuickVpnTest(View v){
        System.out.println("Test trace activity");
        Intent intent = new Intent(OverallMainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void launchDiagnosticTools(View v){
        System.out.println("Test trace activity");
        Intent intent = new Intent(OverallMainActivity.this, MainActivityCon.class);
        startActivity(intent);
    }
}
