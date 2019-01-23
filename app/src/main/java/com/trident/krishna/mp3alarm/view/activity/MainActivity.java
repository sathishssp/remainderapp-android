package com.trident.krishna.mp3alarm.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;

import com.trident.krishna.mp3alarm.R;
import com.trident.krishna.mp3alarm.Utility.Utils;
import com.trident.krishna.mp3alarm.alarmpage;
import com.trident.krishna.mp3alarm.controller.AlarmsManager;
import com.trident.krishna.mp3alarm.model.Alarm;
import com.trident.krishna.mp3alarm.view.AlarmsListArrayAdapter;
import com.trident.krishna.mp3alarm.view.fragment.AddAlarmFragment;
import com.trident.krishna.mp3alarm.view.fragment.AlarmsListFragment;
import com.trident.krishna.mp3alarm.view.fragment.EditAlarmFragment;

import java.util.ArrayList;

public class MainActivity extends Activity implements
		AlarmsListFragment.OnAlarmSelectedListener {

	public static Intent newAlarmsListFragment(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, AlarmsListFragment.class);
		return intent;
	}
	int YOUR_REQUEST_CODE = 200;
	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

	private AlarmsListArrayAdapter alarmAdapter;
	private AlarmsManager alarmsManager;
	private AlarmsListFragment alarmsListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //check if permission request is necessary
		{
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, YOUR_REQUEST_CODE);
		}
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
		}
		initializeAlarmsManager();
		refreshListAdapter();

		if (savedInstanceState == null) {
			if (alarmsListFragment == null) {

				alarmsListFragment = new AlarmsListFragment();

				refreshListAdapter();

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.add(R.id.fragment_container, alarmsListFragment,
						"alarms_list");
				ft.commit();
				fm.executePendingTransactions();

			}
			//
			// if (getIntent().getAction() != null
			// && getIntent().getAction().equals("ringing alarm")) {
			// RingingAlarmFragment ringingAlarmFragment = new
			// RingingAlarmFragment();
			//
			// FragmentManager fm = getFragmentManager();
			// FragmentTransaction ft = fm.beginTransaction();
			//
			// Bundle b = new Bundle();
			// b.putLong(Alarm.INTENT_ID,
			// getIntent().getLongExtra(Alarm.INTENT_ID, -1));
			// ringingAlarmFragment.setArguments(b);
			//
			// ft.replace(R.id.fragment_container, ringingAlarmFragment);
			// ft.commit();
			// fm.executePendingTransactions();
			// }
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_about:
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initializeAlarmsManager();
		getAlarmsManager().openAlarmsDbHelper();
		refreshListAdapter();
	}

	@Override
	protected void onStop() {
		super.onStop();
		getAlarmsManager().close();
	}

	public AlarmsManager getAlarmsManager() {
		return alarmsManager;
	}

	private void initializeAlarmsManager() {
		if (alarmsManager == null)
			alarmsManager = new AlarmsManager(this);
	}

	public void openAddAlarmFragment() {

		AddAlarmFragment addAlarmFragment = new AddAlarmFragment();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.replace(R.id.fragment_container, addAlarmFragment,
				"addAlarmFragment").addToBackStack("addAlarmFragment").commit();
		getFragmentManager().executePendingTransactions();
	}

	@Override
	public void openEditAlarmFragment(int position) {

		EditAlarmFragment editAlarmFragment = new EditAlarmFragment();

		Bundle b = new Bundle();
		b.putInt("alarmPosition", position);

		editAlarmFragment.setArguments(b);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.replace(R.id.fragment_container, editAlarmFragment)
				.addToBackStack(null).commit();
		getFragmentManager().executePendingTransactions();
	}


	@Override
	public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			if(requestCode == YOUR_REQUEST_CODE)
			{

			}
		} else {

		}
	}


	public void onBackPressed(){
//		super.onBackPressed();
		if(Utils.btnLabel.contains("Reminder")){
			finish();
		} else {
			Intent intent = new Intent(MainActivity.this, alarmpage.class);
			overridePendingTransition(R.anim.rotate, R.anim.rotate);
			startActivity(intent);
			finish();
		}

	}

	public void refreshListAdapter() {
		ArrayList<Alarm> alarmList=new ArrayList<>();
		ArrayList<Alarm> finalAlarmList=new ArrayList<>();

		if (alarmAdapter == null) {
			alarmAdapter = new AlarmsListArrayAdapter(this,
					R.layout.alarm_listview_row,alarmList ,
					this);
		}
		if(Utils.btnLabel.contains("Reminder")){
			for(Alarm alarm:alarmsManager.getAllAlarms()){
				if(alarm.getCreate_date().equalsIgnoreCase(Utils.lastSelectedDate) && alarm.getType()==2){
					alarmList.add(alarm);
				}
			}
		} else {
			for(Alarm alarm:alarmsManager.getAllAlarms()){
				if(alarm.getType()==1){
					alarmList.add(alarm);
				}
			}
		}

		alarmAdapter.clear();
		alarmAdapter.addAll(alarmList);
		alarmAdapter.notifyDataSetChanged();

	}

	// public void onDeleteButtonClickListener(View view) {
	// Long alarmId = (Long) view.getTag();
	// alarmsManager.cancelAlarm(alarmId, true);
	// refreshListAdapter();
	// alarmAdapter.notifyDataSetChanged();
	// alarmsListFragment.getListView().invalidateViews();
	// }

	public ListAdapter getListAdaptor() {
		return alarmAdapter;
	}
}
