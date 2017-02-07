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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.strongswan.android.R;
import org.strongswan.android.connector.utils.IpLookupAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    This activity is used to look up data about a given ip/url. The data is requested from
    www.ip-api.com, the data returned is restricted to geolocation data only. However it could
    be extended to a full IP lookup service.
 */

public class IPLookupActivity extends AppCompatActivity {

    String response;
    private TextView address;
    private TextView port;
    private TextView results;
    private URL url;
    private String addressToCheck;
    private String portToCheck;
    private Boolean success;
    private Boolean connectionTimedOut;
    private List<String> lookupData;
    String replacedResponse;

    private ListView listViewIpLookup;
    private IpLookupAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iplookup2);

        address = (TextView) findViewById(R.id.address);
        listViewIpLookup = (ListView) this.findViewById(R.id.listViewIpLookup);

        lookupData = new ArrayList<>();

        adapter = new IpLookupAdapter(this, android.R.layout.simple_list_item_1, lookupData);
        listViewIpLookup.setAdapter(adapter);

    }


    /**
     * Post's request to the target url api for ip lookup. Uses the HTTP url connection object,
     * currently does not support HTTPS. Starts a new thread to conduct the operation before
     * merging back into the app thread. The response is passed to a buffered writer and converted
     * into JSON for ease of use.
     */
    public void discover(View v) {
        success = false;
        connectionTimedOut = false;

        addressToCheck = address.getText().toString();
        lookupData.clear();

        //Hide softkeyboard
        hideKeyboard();
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    // Construct api url
                    url = new URL("http://ip-api.com/json/" + addressToCheck);
                    Log.d(IPLookupActivity.class.getSimpleName(), "Address to check is: " + addressToCheck);

                    Log.d(IPLookupActivity.class.getSimpleName(), "Resolved url is: " + url);

                    // Create a urlConnection object
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setConnectTimeout(5000); //set timeout to 5 seconds

                    // Get the response from the api and process it into a useable string.
                    response = readInputStreamToString(urlConnection);

                    // Clean up the response string so that it can be interpreted by JSON.
                    JSONObject jsonResponse = new JSONObject(response.replaceAll("\\\"","\\\'"));

                    Log.d(IPLookupActivity.class.getSimpleName(), "Response: " + response);

                    // Add each key/value to a List for use in a list view.
                    Iterator<String> iter = jsonResponse.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();

                        try {
                            Object value = jsonResponse.get(key);
//                            System.out.println(key + " : " + value);
                            lookupData.add(key + " : " + value);
                        } catch (JSONException e) {
                            // Something went wrong!
                            Log.e(IPLookupActivity.class.getSimpleName(), "An error occured when processing the response... " + e);

                        }
                    }
                    // Clean up the connection
                    urlConnection.disconnect();

                } catch (java.net.SocketTimeoutException e) {
                    Log.e(IPLookupActivity.class.getSimpleName(), "Connection timed out..." + e);
                    success = false;
                    connectionTimedOut = true;

                } catch (Exception e) {
                    Log.e(IPLookupActivity.class.getSimpleName(), "Something else went wrong... " + e);
                } finally {
//            urlConnection.disconnect();
                }


            }
        });

        thread.start();
        try{
            // Wait for thread to finish before updating view.
            thread.join();
            if (addressToCheck == "" || addressToCheck.isEmpty()){
                Toast.makeText(IPLookupActivity.this, "Address was blank.. Ran query for this device's IP", Toast.LENGTH_SHORT).show();
            }
            createListView(lookupData);
        }catch (Exception e){
            System.out.println(e);
        }

        Log.d(IPLookupActivity.class.getSimpleName(), "Running results update");

        //TODO: If needed manipulate the listview here.
//        if (success){
//
//
//        }else {
//
//        }
    }

    /*
     Reads the response from the html connection and returns it in a usable string.
     * @param connection
     *          The connection object used for the http call.
    */

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

            }
            result = sb.toString();
        }
        catch (Exception e) {
            Log.e(IPLookupActivity.class.getSimpleName(), "An error occured with the string buffer.. Most likely returned null" + e);
            result = null;
        }
        finally {
            if (is != null) {

                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.e(IPLookupActivity.class.getSimpleName(), "An error occured" + e);
                }
            }
        }

        return result;
    }

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // Create the listview to display the data returned by the ip lookup.
    public void createListView(final List data){

        adapter = new IpLookupAdapter(this, android.R.layout.simple_list_item_1, data);
        listViewIpLookup.setAdapter(adapter);

        //TODO: This can be used to allow users to select a listview item and expand it to get more data or launch a google map with a location pin.

//        listViewIpLookup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view,
//                                    int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(2000).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                data.remove(item);
//                                adapter.notifyDataSetChanged();
//                                view.setAlpha(1);
//                            }
//                        });
//            }
//
//        });
    }

}
