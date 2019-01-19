package com.trident.krishna.mp3alarm.viewtwelve.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.trident.krishna.mp3alarm.Utility.Utils;

import java.util.Calendar;

public class AddAlarmFragment extends AlarmFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	protected OnClickListener getCreateAlarmButtonClickListener() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {

				timePicker.clearFocus();
				int hour = timePicker.getCurrentHour();
				int minute = timePicker.getCurrentMinute();
				String timeString = "";
				if(!TextUtils.isEmpty(Utils.selectedAlarmType)){
					Calendar calendar = Calendar.getInstance();
					if(Utils.selectedAlarmType.equalsIgnoreCase("1")){
						calendar.add(Calendar.HOUR_OF_DAY,1);
					}else if(Utils.selectedAlarmType.equalsIgnoreCase("12")){
						calendar.add(Calendar.HOUR_OF_DAY,12);
					} else {
						calendar.add(Calendar.HOUR_OF_DAY, 24);
					}
					hour=calendar.get(Calendar.HOUR);
					minute=calendar.get(Calendar.MINUTE);
				}
				if (hour <= 9)
					timeString = "0";
				timeString += hour + ":";
				if (minute <= 9)
					timeString += "0";
				timeString += minute;

				String nameString = alarmNameET.getText().toString();

				String musicString = "";
				if (tempMusicFilePath != null) {
					musicString = tempMusicFilePath;
				} else if (tempRecording) {
					musicString = "";
				}

				if (!getMainActivity().getAlarmsManager()
						.checkForDuplicateAlarm(timeString)) {

					getMainActivity().getAlarmsManager().createAlarm(
							timeString, mo.isChecked(), tu.isChecked(),
							we.isChecked(), th.isChecked(), fr.isChecked(),
							sa.isChecked(), su.isChecked(), nameString,
							musicString);

					AddAlarmFragment.this.refreshAndClose();
				}

			}
		};
	}
}
