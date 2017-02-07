package org.strongswan.android.connector.utils;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by haxor on 7/02/17.
 */

/*
Utility object to run an Async task for port availibility checking.
 */

public class ExecuteAsyncTestPort extends AsyncTask {

    private String host;
    private int port;
    private int timeout;
    private ProgressBar progressBarPing;
    private TextView results;
    private Boolean success;

    /*
    Object to contain the required attributes to conduct the port scan and the UI objects to update.
     */
    public ExecuteAsyncTestPort(String host, int port, int timeout, ProgressBar progressBarPing, TextView results){
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.progressBarPing = progressBarPing;
        this.results = results;
        success = false;

    }

    // Start the progress bar and reset the results UI element to transparent.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(this.getClass().getSimpleName(), "Starting background task");
        startProgressBar();
        results.setBackgroundColor(Color.TRANSPARENT);
    }

    // Execute the port scan on a new thread.
    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d(this.getClass().getSimpleName(), "Executing background task");
        success = pingHost(host, port, timeout);
        return null;
    }

    // Stop the progress bar and update the UI with the results.
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d(this.getClass().getSimpleName(), "Finished background task");
        stopProgressBar();

        if (success) {
            Log.d(this.getClass().getSimpleName(), "Updating UI succesful");
            results.setBackgroundColor(Color.GREEN);
            results.setTextColor(Color.BLACK);
            results.setText(host + " is reachable on port " + port);

        } else {
            Log.d(this.getClass().getSimpleName(), "Updating UI fail");
            results.setBackgroundColor(Color.RED);
            results.setText(host + " is not reachable on port " + port);

        }
    }

    /*
    Ping the specified host and port using a simple socket connection. No actual connection is
    required, we are simply attempting to see if the host is reachable on the given port. If a
    connection is not possible an exeption is thrown. We do not really care why it wasnt reachable.
     */
    public boolean pingHost(String host, int port, int timeout) {
        Log.d(this.getClass().getSimpleName(), "Executing port check for: " + host + ":" + port + ", with a " + String.valueOf(timeout) + " timeout");
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            socket.close();
            Log.d(this.getClass().getSimpleName(), "Port is available");
            return true;
            // Either timeout or unreachable or failed DNS lookup.
        } catch (IOException e) {
            Log.d(this.getClass().getSimpleName(), "Port is not available");
            return false;
        }
    }

    public void startProgressBar() {
        progressBarPing.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        progressBarPing.setVisibility(View.GONE);
    }
}
