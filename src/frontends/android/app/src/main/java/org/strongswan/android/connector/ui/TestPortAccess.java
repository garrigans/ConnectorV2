/**
 * @author haxor on 4/11/16.
 */

package org.strongswan.android.connector.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.strongswan.android.R;
import org.strongswan.android.connector.utils.ExecuteAsyncTestPort;

/*
    This activity is used to test whether a url/ip can be reached via various ports. It is intended
    to allow users the ability to further test what may be blocking at a specified url.
 */
public class TestPortAccess extends AppCompatActivity {

    private TextView address;
    private TextView port;
    private TextView results;
    private Button testPort;
    private String addressToCheck;
    private String portToCheck;
    private Boolean portFieldEmpty;
    private ProgressBar progressBarPing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_port_access2);

        address = (TextView) findViewById(R.id.address);
        port = (TextView) findViewById(R.id.port);
        results = (TextView) findViewById(R.id.portAccessResults);
        results.setSingleLine(false);
        testPort = (Button) findViewById(R.id.testPort);
        progressBarPing = (ProgressBar) findViewById(R.id.progressBarPing);

    }

    /*
    Attempts to establish a socket connection via the specified url with a port number appended.
     */

    public void testPort(View v) {
        Log.d(this.getClass().getSimpleName(), "Testing port availability");
        results.setText("");
        addressToCheck = address.getText().toString();
        portToCheck = port.getText().toString();
        portFieldEmpty = false;

        //Hide softkeyboard
        hideKeyboard();

        if (portToCheck.isEmpty() || portToCheck.equals("")) {
            portToCheck = "80";
            portFieldEmpty = true;
        }

        // Launch an Async task to conduct the port check, this allows the app to update the UI whilst
        // the scan is running.
        new ExecuteAsyncTestPort(addressToCheck, Integer.valueOf(portToCheck), 5000, progressBarPing, results).execute();

        if (portFieldEmpty) {
            Toast.makeText(this, "Port field was empty used :80", Toast.LENGTH_SHORT).show();
        }
    }

    // Hide the keyboard from the screen.
    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public TextView getAddress() {
        return address;
    }

    public void setAddress(TextView address) {
        this.address = address;
    }

    public TextView getPort() {
        return port;
    }

    public void setPort(TextView port) {
        this.port = port;
    }



}
