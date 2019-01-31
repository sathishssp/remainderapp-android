package com.trident.krishna.mp3alarm.view.fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.trident.krishna.mp3alarm.R;
import com.trident.krishna.mp3alarm.Utility.Utils;
import com.trident.krishna.mp3alarm.view.activity.MainActivity;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class AlarmsListFragment extends ListFragment {
	InterstitialAd mInterstitialAd;
	public interface OnAlarmSelectedListener {
		public void openEditAlarmFragment(int position);
	}

	private OnAlarmSelectedListener callback;
	private RelativeLayout rootView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			callback = (OnAlarmSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SelectionListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (RelativeLayout) inflater.inflate(R.layout.alarms_listview,
				container, false);

		Button addNewAlarmBtn = (Button) rootView
				.findViewById(R.id.btn_add_alarm);
		addNewAlarmBtn.setText(Utils.btnLabel);
		addNewAlarmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getMainActivity().openAddAlarmFragment();
			}
		});
		setListAdapter(getMainActivity().getListAdaptor());

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mInterstitialAd = new InterstitialAd(getActivity());
		mInterstitialAd.setAdUnitId("ca-app-pub-6095264288861112/2428964823");
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		callback.openEditAlarmFragment(position);
	}

	public MainActivity getMainActivity() {
		return (MainActivity) super.getActivity();
	}
}
