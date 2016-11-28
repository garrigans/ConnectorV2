package org.strongswan.android.connector.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.strongswan.android.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPortAccess extends AppCompatActivity {

    String response;
    private TextView address;
    private TextView port;
    private TextView results;
    private URL url;
    private String addressToCheck;
    private String portToCheck;
    private Boolean success;
    private Boolean connectionTimedOut;
    private ArrayList<String> connectionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_port_access2);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        address = (TextView) findViewById(R.id.address);
        port = (TextView) findViewById(R.id.port);
        results = (TextView) findViewById(R.id.portAccessResults);
        results.setSingleLine(false);

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

    public void testPort(View v) {
        System.out.println("Test Port click");

        connectionData = new ArrayList<>();
        success = false;
        connectionTimedOut = false;
        results.setText("");
        addressToCheck = address.getText().toString();
        portToCheck = port.getText().toString();

        //Hide softkeyboard
        hideKeyboard();
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {



                try {
                    if (portToCheck.isEmpty() || portToCheck.equals("")){
                        portToCheck = "80";
                    }
                    if (checkForIp(addressToCheck)){
                        url = new URL("http://" + addressToCheck + ":" + portToCheck);

                    }else{
                        if (addressToCheck.contains("www.")){
                            url = new URL("http://" + addressToCheck + ":" + portToCheck);
                        }else if (!addressToCheck.contains("www.")){
                            url = new URL("http://www." + addressToCheck + ":" + portToCheck);
                        }

                    }


//                    URL url = new URL("http://203.6.24.5/");


//
                    System.out.println(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setConnectTimeout(5000); //set timeout to 5 seconds


                    System.out.println(readInputStreamToString(urlConnection));
//                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    System.out.println("here");

                    int code = urlConnection.getResponseCode();
                    if (code >= 200 && code <300){
                        success = true;
                    }else if (code >= 300 && code <400){
                        success = true;
                    }else{
                        success = false;
                    }

                    connectionData.add("RESPONSE: " + String.valueOf(urlConnection.getResponseCode()));
                    connectionData.add("MSG: " + urlConnection.getResponseMessage());
                    connectionData.add("URL: " + urlConnection.getURL().toString());

                    if (urlConnection.getURL().getPort() == -1){
                        connectionData.add("PORT: Abstracted due to https redirect");
                    }else{
                        connectionData.add("PORT: " + String.valueOf(urlConnection.getURL().getPort()));
                    }

                    connectionData.add("PROTOCOL: " + urlConnection.getURL().getProtocol());


                    urlConnection.disconnect();

                } catch (java.net.SocketTimeoutException e) {
                    System.out.println("Connection timed out.." + e);
                    success = false;
                    connectionTimedOut = true;

                } catch (Exception e) {
                    System.out.println(e);
                } finally {

//            urlConnection.disconnect();
                }


            }
        });

        thread.start();
        try{
            // Wait for thread to finish before updating view.
            thread.join();
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println("Running results update");
        for (String s : connectionData){
            System.out.println("Connection data.." + s);
            results.append(s + "\n");
        }



        if (success){
            results.setBackgroundColor(Color.GREEN);
            results.setTextColor(Color.BLACK);

        }else {
            results.setBackgroundColor(Color.RED);
            if (connectionTimedOut){
                results.setText("Connection timed out");

            }

        }
    }

    private String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
                System.out.println(inputLine);
            }
            System.out.println(sb.toString());
            result = sb.toString();
        }
        catch (Exception e) {
            System.out.println(e);
            result = null;
        }
        finally {
            if (is != null) {
                System.out.println("null");
                try {
                    is.close();
                }
                catch (IOException e) {
                    System.out.println(e);
                }
            }
        }

        return result;
    }

    public static boolean checkForIp(String text) {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(text);
        return m.find();
    }

    public void hideKeyboard(){
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

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getAddressToCheck() {
        return addressToCheck;
    }

    public void setAddressToCheck(String addressToCheck) {
        this.addressToCheck = addressToCheck;
    }

    public String getPortToCheck() {
        return portToCheck;
    }

    public void setPortToCheck(String portToCheck) {
        this.portToCheck = portToCheck;
    }
}
