package com.example.krishna.mp3alarm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.krishna.mp3alarm.AddToDo.AddToDoFragment;
import com.example.krishna.mp3alarm.Maintodo.MainFragment;
import com.example.krishna.mp3alarm.Utility.Utils;
import com.example.krishna.mp3alarm.resourceManagermms.ResourceManager;
import com.example.krishna.mp3alarm.smsManagermms.SMSManager;
import com.example.krishna.mp3alarm.DBHelp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.getActivity;
import static com.example.krishna.mp3alarm.AddToDo.AddToDoFragment.formatDate;

public class AddSchedulemms extends AppCompatActivity {
    FloatingActionButton dateChangeButton;
    FloatingActionButton timeChangeButton;
    FloatingActionButton changeImageButton;
    FloatingActionButton playSoundButton;
    FloatingActionButton nextButton;
    ImageView selectedImageView;
    TextView timeTextView;
    TextView dateTextView;
    Spinner soundListSpinner;
    Spinner phoneStatusSpinner;
    EditText messageEditText;

    int mYear, mMonth, mDay;
    int mHour, mMinute, mAmPm;
    Calendar mCurrentDate;
    int imageResourceID;
    int selectedSoundID;
    MediaPlayer mediaPlayer;

    private final int SELECT_IMAGE_REQUEST_CODE = 1;
    private Activity activity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.krishna.mp3alarm.R.layout.activity_add_schedulemms);
        initializeVariables();
     //   setCurrentDateText();
//        setCurrentTimeText();

        dateChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AddSchedulemms.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker datepicker, int
                                    selectedYear, int selectedMonth, int selectedDay) {
                                mCurrentDate.set(Calendar.YEAR, selectedYear);
                                mCurrentDate.set(Calendar.MONTH, selectedMonth);
                                mCurrentDate.set(Calendar.DAY_OF_MONTH, selectedDay);
                                setCurrentDateText();
                            }
                        }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        timeChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimePickerDialog mTimePicker;
                Date date = new Date();
                final Date finalDate = date;
                final int[] hour = new int[1];
                int minute = 0;
                mTimePicker = new TimePickerDialog(AddSchedulemms.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                              //  mCurrentDate.set(Calendar.HOUR, hourOfDay);
                              //  mCurrentDate.set(Calendar.MINUTE, minute);

//                                setCurrentTimeText();
                                Date date;
                date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                 hour[0] = calendar.get(Calendar.HOUR_OF_DAY);
                 minute = calendar.get(Calendar.MINUTE);
                            }
                        }, hour[0], minute, false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a",
                        Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
             //String tempTime = dateFormat.format(calendar.getTime());
                String formatToUse = new String();
                String userTime = formatDate(formatToUse, date);
                timeTextView.setText(userTime);
            }
        });



        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSchedulemms.this, SelectImagemms.class);
                startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
            }
        });

        soundListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSoundID = (int) soundListSpinner.getSelectedItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        playSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    playSoundButton.setImageResource(com.example.krishna.mp3alarm.R.drawable.play_sound_icon);
                } else {
                    playSoundButton.setImageResource(com.example.krishna.mp3alarm.R.drawable.sound_stop_icon);

                    int soundResourceID = ResourceManager.getSoundFromID(selectedSoundID);
                    mediaPlayer = MediaPlayer.create(AddSchedulemms.this,
                            soundResourceID);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playSoundButton.setImageResource(com.example.krishna.mp3alarm.R.drawable.play_sound_icon);
                        }
                    });
                }

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMessageValidated(messageEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "You can not use '>' character in message!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                int mappedImageResourceID = ResourceManager.mapImageResource(imageResourceID);
                int mappedPhoneStatus = ResourceManager.mapPhoneStatusFromName(phoneStatusSpinner
                        .getSelectedItem().toString());

                SMSManager smsManager = new SMSManager();
                smsManager.setImage(String.valueOf(mappedImageResourceID));
                smsManager.setSound(String.valueOf(selectedSoundID));
                smsManager.setMessage(messageEditText.getText().toString());
                smsManager.setPhoneStatus(String.valueOf(mappedPhoneStatus));
              //smsManager.setTimestamp(String.valueOf(mCurrentDate.getTimeInMillis()));

                Intent intent = new Intent(AddSchedulemms.this, SetSchedulemms.class);
                intent.putExtra(Constantsmms.SMS_MANAGER, smsManager);
                startActivity(intent);
            }
        });
    }


    private void initializeVariables() {
        dateChangeButton = findViewById(com.example.krishna.mp3alarm.R.id.date_change_button);
        timeChangeButton = findViewById(com.example.krishna.mp3alarm.R.id.time_change_button);
        changeImageButton = findViewById(com.example.krishna.mp3alarm.R.id.image_change_button);
        playSoundButton = findViewById(com.example.krishna.mp3alarm.R.id.sound_play_button);
        nextButton = findViewById(com.example.krishna.mp3alarm.R.id.save_button);
        dateTextView = findViewById(com.example.krishna.mp3alarm.R.id.date_text_view);
        timeTextView = findViewById(com.example.krishna.mp3alarm.R.id.time_text_view);
        selectedImageView = findViewById(com.example.krishna.mp3alarm.R.id.selected_image_view);
        soundListSpinner = findViewById(com.example.krishna.mp3alarm.R.id.sound_list_spinner);
        phoneStatusSpinner = findViewById(com.example.krishna.mp3alarm.R.id.phone_status_spinner);
        messageEditText = findViewById(com.example.krishna.mp3alarm.R.id.message_edit_text);
    //    mCurrentDate = Calendar.getInstance();

        // Default values
        imageResourceID = ResourceManager.getMappedImageResourceID(0);
        selectedSoundID = ResourceManager.getSoundFromID(0);
        Intent mIntent = getIntent();
        int dayOfMonth = mIntent.getIntExtra("dayOfMonth",0);
        int month = mIntent.getIntExtra("month", 0);
        int year = mIntent.getIntExtra("year", 0);
        String[] selectedDate= Utils.lastSelectedDate.split("/");
        if(selectedDate!=null && selectedDate.length>0){
            dayOfMonth=Integer.parseInt(selectedDate[0]);
            month=Integer.parseInt(selectedDate[1]);
            year=Integer.parseInt(selectedDate[2]);
        }

        dateTextView.setText(dayOfMonth+"-"+month+"-"+year);
        setSoundSpinnerTexts();
    }

    private void setSoundSpinnerTexts() {
      /*  List<String> spinnerArray = new ArrayList<>();
        spinnerArray.addAll(Arrays.asList(ResourceManager.soundNames));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundListSpinner.setAdapter(adapter);*/

        DBHelpers db = new DBHelpers(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        soundListSpinner.setAdapter(dataAdapter);
    }

    private void setCurrentDateText() {
        mYear = mCurrentDate.get(Calendar.YEAR);
        mMonth = mCurrentDate.get(Calendar.MONTH);
        mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        // Since month is 0 index based, adding 1 with selected month
        String tempDate = "" + mDay + "/" + (mMonth + 1) + "/" + mYear;
        dateTextView.setText(tempDate);
    }

    private void setCurrentTimeText() {
        mHour = mCurrentDate.get(Calendar.HOUR);
        mMinute = mCurrentDate.get(Calendar.MINUTE);
        mAmPm = mCurrentDate.get(Calendar.AM_PM);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a",
                Locale.ENGLISH);
        String tempTime = dateFormat.format(mCurrentDate.getTime());
        timeTextView.setText(tempTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageResourceID = data.getIntExtra(Constantsmms.IMAGE_RESOURCE_CODE, 0);
                setImageFromResource(imageResourceID);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Default value
                imageResourceID = com.example.krishna.mp3alarm.R.drawable.pic1;
            }
        }
    }

    private void setImageFromResource(int imageResourceCode) {
        selectedImageView.setImageResource(imageResourceCode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    private boolean isMessageValidated(String message) {
        // Checking if '>' contains in the message
        // since that character was considered as separator
        int index = message.indexOf('>');
        if (index != -1)
            return false;
        return true;
    }

    public Activity getActivity() {
        return activity;
    }

    public Context getContext() {
        return context;
    }
}
