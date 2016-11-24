/*
This file is part of the project TraceroutePing, which is an Android library
implementing Traceroute with ping under GPL license v3.
Copyright (C) 2013  Olivier Goutay

TraceroutePing is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

TraceroutePing is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with TraceroutePing.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.projects.haxor.connector.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.haxor.connector.enums.ResusltStatus;
import com.projects.haxor.connector.enums.RouteStatus;
import com.projects.haxor.connector.network.TracerouteContainer;
import com.projects.haxor.connector.network.TracerouteWithPing;

import org.strongswan.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The main activity
 * 
 * @author Olivier Goutay
 * 
 */
public class TraceActivity extends AppCompatActivity {

	public static final String tag = "TraceroutePing";
	public static final String INTENT_TRACE = "INTENT_TRACE";

	private Button buttonLaunch;
	private Button buttonTestVpn;
	private EditText editTextPing;
	private ProgressBar progressBarPing;
	private ListView listViewTraceroute;
	private TraceListAdapter traceListAdapter;
	private TextView textViewOverallStatus;

	private TracerouteWithPing tracerouteWithPing;
	private final int maxTtl = 40;

	private List<TracerouteContainer> traces;

	/**
	 * onCreate, init main components from view
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

//		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setDisplayShowHomeEnabled(true);

		getWindow().setBackgroundDrawableResource(R.drawable.grey_texture) ;

		this.tracerouteWithPing = new TracerouteWithPing(this);
		this.traces = new ArrayList<TracerouteContainer>();

		this.buttonLaunch = (Button) this.findViewById(R.id.buttonLaunch);
		this.buttonTestVpn = (Button) this.findViewById(R.id.buttonTestVpn);
		this.editTextPing = (EditText) this.findViewById(R.id.editTextPing);
		this.listViewTraceroute = (ListView) this.findViewById(R.id.listViewTraceroute);
		this.progressBarPing = (ProgressBar) this.findViewById(R.id.progressBarPing);
		this.textViewOverallStatus = (TextView) this.findViewById(R.id.textViewOverallStatus);

		initView();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.toolbar_actions, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case R.id.action_settings:
//				// User chose the "Settings" item, show the app settings UI...
//				return true;
//
//			case R.id.action_favorite:
//				// User chose the "Favorite" action, mark the current item
//				// as a favorite...
//				return true;
//
//			default:
//				// If we got here, the user's action was not recognized.
//				// Invoke the superclass to handle it.
//				return super.onOptionsItemSelected(item);
//
//		}
//	}

	/**
	 * initView, init the main view components (action, adapter...)
	 */
	private void initView() {
		buttonLaunch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editTextPing.getText().length() == 0) {
					Toast.makeText(TraceActivity.this, getString(R.string.no_text), Toast.LENGTH_SHORT).show();
				} else {
					removeTestVpnButton();
					textViewOverallStatus.setText("Launching...");
					traces.clear();
					traceListAdapter.notifyDataSetChanged();
					startProgressBar();
					hideSoftwareKeyboard(editTextPing);

					// Fire off the trace route for user entered url/ip
					tracerouteWithPing.executeTraceroute(editTextPing.getText().toString(), maxTtl);
				}
			}
		});

		traceListAdapter = new TraceListAdapter(getApplicationContext());
		listViewTraceroute.setAdapter(traceListAdapter);
	}

	/**
	 * Allows to refresh the listview of traces
	 * 
	 * @param trace
	 *            The list of traces to refresh
	 */
	public void refreshList(TracerouteContainer trace) {
		final TracerouteContainer fTrace = trace;
        runOnUiThread(new Runnable() {
			@Override
			public void run() {
				traces.add(fTrace);
				traceListAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * The adapter of the listview (build the views)
	 */
	public class TraceListAdapter extends BaseAdapter {

		private Context context;

		public TraceListAdapter(Context c) {
			context = c;
		}

		public int getCount() {
			return traces.size();
		}

		public TracerouteContainer getItem(int position) {
			return traces.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			// first init
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.item_list_trace, null);

				TextView textViewNumber = (TextView) convertView.findViewById(R.id.textViewNumber);
				TextView textViewIp = (TextView) convertView.findViewById(R.id.textViewIp);
				TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
				ImageView imageViewStatusPing = (ImageView) convertView.findViewById(R.id.imageViewStatusPing);



				// Set up the ViewHolder.
				holder = new ViewHolder();
				holder.textViewNumber = textViewNumber;
				holder.textViewIp = textViewIp;
				holder.textViewTime = textViewTime;
				holder.imageViewStatusPing = imageViewStatusPing;

				// Store the holder with the view.
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TracerouteContainer currentTrace = getItem(position);

			if (position % 2 == 1) {

				convertView.setBackgroundResource(R.drawable.table_odd_lines);
			} else {
				convertView.setBackgroundResource(R.drawable.table_pair_lines);
			}

			if (currentTrace.isSuccessful()) {
				holder.imageViewStatusPing.setImageResource(R.drawable.check);


			} else {
				holder.imageViewStatusPing.setImageResource(R.drawable.cross);

			}



			holder.textViewNumber.setText(position + "");
			holder.textViewIp.setText(currentTrace.getHostname() + " (" + currentTrace.getIp() + ")");
			holder.textViewTime.setText(currentTrace.getMs() + "ms");

			if (tracerouteWithPing.getTraceRouteStatus() != RouteStatus.FINISH){
				textViewOverallStatus.setText("In progress...");
				parent.setBackground(null);
			}else if (tracerouteWithPing.getTraceRouteStatus() == RouteStatus.FINISH){
				textViewOverallStatus.setText("Finished..." + tracerouteWithPing.getTraceResultStatus());
				if(tracerouteWithPing.getTraceResultStatus() == ResusltStatus.REACHABLE){
					showTestVpnButton();

					parent.setBackgroundColor(Color.GREEN);

				}
				else if (tracerouteWithPing.getTraceResultStatus() == ResusltStatus.UNREACHABLE){
					parent.setBackgroundColor(Color.RED);
				}

			}


			return convertView;
		}

		// ViewHolder pattern
		class ViewHolder {
			TextView textViewNumber;
			TextView textViewIp;
			TextView textViewTime;
			ImageView imageViewStatusPing;

		}
	}

	/**
	 * Hides the keyboard
	 * 
	 * @param currentEditText
	 *            The current selected edittext
	 */
	public void hideSoftwareKeyboard(EditText currentEditText) {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(currentEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

//	public void launchTestVpnActivity(View v){
//		System.out.println("Test VPN");
//		Intent intent = new Intent(TraceActivity.this, TestVpnActivity.class);
//		startActivity(intent);
//	}

	public void startProgressBar() {
		progressBarPing.setVisibility(View.VISIBLE);
	}

	public void stopProgressBar() {
		progressBarPing.setVisibility(View.GONE);
	}

	public void showTestVpnButton() { buttonTestVpn.setVisibility(View.VISIBLE);}

	public void removeTestVpnButton() { buttonTestVpn.setVisibility(View.GONE);}

//	public void setBackgroundColour(int colour){
//		findViewById(android.R.id.content).setBackgroundColor(colour);
//	}

}
