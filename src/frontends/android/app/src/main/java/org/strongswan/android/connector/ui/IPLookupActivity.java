package org.strongswan.android.connector.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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





//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);



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

    public void discover(View v) {
        System.out.println("Test Port click");


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


                    url = new URL("http://ip-api.com/json/" + addressToCheck);
                    System.out.println("Address to check is: " + addressToCheck);

                    System.out.println(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setConnectTimeout(5000); //set timeout to 5 seconds

                    response = readInputStreamToString(urlConnection);

                    JSONObject jsonResponse = new JSONObject(response.replaceAll("\\\"","\\\'"));

                    System.out.println(jsonResponse.get("as"));
                    Iterator<String> iter = jsonResponse.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();

                        try {
                            Object value = jsonResponse.get(key);
                            System.out.println(key + " : " + value);
                            lookupData.add(key + " : " + value);
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }

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
            if (addressToCheck == "" || addressToCheck.isEmpty()){
                Toast.makeText(IPLookupActivity.this, "Address was blank.. Ran query for this device's IP", Toast.LENGTH_SHORT).show();
            }
            createListView(lookupData);
        }catch (Exception e){
            System.out.println(e);
        }


        System.out.println("Running results update");




        if (success){


        }else {

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

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void createListView(final List data){

        adapter = new IpLookupAdapter(this, android.R.layout.simple_list_item_1, data);
        listViewIpLookup.setAdapter(adapter);

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
