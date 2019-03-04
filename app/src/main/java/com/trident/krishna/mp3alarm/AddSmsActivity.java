package com.trident.krishna.mp3alarm;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trident.krishna.mp3alarm.Utility.DateFormatConversion;
import com.trident.krishna.mp3alarm.Utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AddSmsActivity extends Activity {
//    DatePicker datePicker;
    ImageView speaker;
    EditText formMessage;
    TextView txtDate;


    final public static int RESULT_SCHEDULED = 1;
    final public static int RESULT_UNSCHEDULED = 2;
    final public static String INTENT_SMS_ID = "INTENT_SMS_ID";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    final private static String SMS_STATE = "SMS_STATE";
    final private static int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    final private String[] permissionsRequired = new String[] {
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS
    };

    private SmsModel sms;
    private ArrayList<String> permissionsGranted = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivityForResult(new Intent(this, SmsSchedulerPreferenceActivity.class), 1);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionsGranted()) {
            buildForm();
        }
    }

    private void findDatePicker(){
//        datePicker = (DatePicker)findViewById(R.id.form_date);
    }

    private void buildForm() {
        findDatePicker();
        EditText formMessage = findViewById(R.id.form_input_message);
        AutoCompleteTextView formContact = findViewById(R.id.form_input_contact);
        TextWatcher watcherEmptiness = new EmptinessTextWatcher(this, formContact, formMessage);
        formContact.addTextChangedListener(watcherEmptiness);
        formMessage.addTextChangedListener(watcherEmptiness);

        new BuilderMessage().setView(formMessage).setSms(sms).build();
        new BuilderContact().setView(formContact).setSms(sms).setActivity(this).build();

        new BuilderSimCard().setActivity(this).setView(findViewById(R.id.form_sim_card)).setSms(sms).build();
        new BuilderRecurringMode()
            .setRecurringDayView((Spinner) findViewById(R.id.form_recurring_day))
            .setRecurringMonthView((Spinner) findViewById(R.id.form_recurring_month))
//            .setDateView(datePicker)
            .setActivity(this)
            .setView(findViewById(R.id.form_recurring_mode))
            .setSms(sms)
            .build()
        ;

        new BuilderTime().setActivity(this).setView(findViewById(R.id.form_time)).setSms(sms).build();
//        new BuilderDate().setActivity(this).setView(findViewById(R.id.form_date)).setSms(sms).build();

        new BuilderCancel().setView(findViewById(R.id.button_cancel)).setSms(sms).build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DbHelper.closeDbHelper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DbHelper.closeDbHelper();
    }
    Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sms);

        findDatePicker();

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



        cal.set(year,  month-1, dayOfMonth);
        int xxday = cal.get(Calendar.DATE);
        int xxmonth = cal.get(Calendar.MONTH);
        int xxyear = cal.get(Calendar.YEAR);

//        datePicker.init(xxyear, xxmonth, xxday, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//            }
//        });
//        datePicker.updateDate(xxyear,xxmonth,xxday);
        txtDate=findViewById(R.id.txtDate);
        txtDate.setText(dayOfMonth+"-"+month+"-"+year);
        speaker =(ImageView)findViewById(R.id.speaker);
        formMessage =(EditText) findViewById(R.id.form_input_message);

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });



        long smsId = getIntent().getLongExtra(INTENT_SMS_ID, 0L);
        if (smsId > 0) {
            sms = DbHelper.getDbHelper(this).get(smsId);
        } else if (null != savedInstanceState) {
            sms = savedInstanceState.getParcelable(SMS_STATE);
        }
        if (null == sms) {
            sms = new SmsModel();
        }
//        buildForm();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    formMessage.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != sms) {
            outState.putParcelable(SMS_STATE, sms);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null == sms) {
            sms = savedInstanceState.getParcelable(SMS_STATE);
        }
        if (null == sms) {
            sms = new SmsModel();
        }
    }

    public void scheduleSms(View view) {
        if (!validateForm()) {
            return;
        }
        sms.setStatus(SmsModel.STATUS_PENDING);
        DbHelper.getDbHelper(this).save(sms);
        new Scheduler(getApplicationContext()).schedule(sms);
        setResult(RESULT_SCHEDULED, new Intent());
        finish();
    }

    public void unscheduleSms(View view) {
        DbHelper.getDbHelper(this).delete(sms.getTimestampCreated());
        new Scheduler(getApplicationContext()).unschedule(sms.getTimestampCreated());
        setResult(RESULT_UNSCHEDULED, new Intent());
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
//        cal.setTimeInMillis();
        String datetime=txtDate.getText().toString()+" "+sms.getCalendar().get(GregorianCalendar.HOUR_OF_DAY)+":"+sms.getCalendar().get(GregorianCalendar.MINUTE)+":00";
        long milliseconds=DateFormatConversion.getTimeInMilliSeconds(datetime,"dd-MM-yyyy HH:mm:ss");

        sms.setTimestampScheduled(milliseconds);
        if (sms.getTimestampScheduled() < GregorianCalendar.getInstance().getTimeInMillis()) {
            Toast.makeText(getApplicationContext(), getString(R.string.form_validation_datetime), Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (TextUtils.isEmpty(sms.getRecipientNumber())) {
            ((AutoCompleteTextView) findViewById(R.id.form_input_contact)).setError(getString(R.string.form_validation_contact));
            result = false;
        }
        if (TextUtils.isEmpty(sms.getMessage())) {
            ((EditText) findViewById(R.id.form_input_message)).setError(getString(R.string.form_validation_message));
            result = false;
        }
        return result;
    }

    private boolean permissionsGranted() {
        boolean granted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String required : this.permissionsRequired) {
                if (checkSelfPermission(required) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(required);
                } else {
                    this.permissionsGranted.add(required);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                granted = false;
                String[] notGrantedArray = permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]);
                requestPermissions(notGrantedArray, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }
        return granted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                List<String> requiredPermissions = Arrays.asList(this.permissionsRequired);
                String permission;
                for (int i = 0; i < permissions.length; i++) {
                    permission = permissions[i];
                    if (requiredPermissions.contains(permission) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        this.permissionsGranted.add(permission);
                    }
                }
                if (this.permissionsGranted.size() == this.permissionsRequired.length) {
                   buildForm();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
