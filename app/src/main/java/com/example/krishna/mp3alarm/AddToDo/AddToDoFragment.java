package com.example.krishna.mp3alarm.AddToDo;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krishna.mp3alarm.Analytics.AnalyticsApplication;
import com.example.krishna.mp3alarm.AppDefault.AppDefaultFragment;
import com.example.krishna.mp3alarm.Maintodo.MainFragment;
import com.example.krishna.mp3alarm.R;
import com.example.krishna.mp3alarm.Utility.ToDoItem;
import com.example.krishna.mp3alarm.Utility.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class AddToDoFragment extends AppDefaultFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Date mLastEdited;
    private EditText mToDoTextBodyEditText1,mToDoTextBodyEditText2,mToDoTextBodyEditText3,mToDoTextBodyEditText4,mToDoTextBodyEditText5;
    private SwitchCompat mToDoDateSwitch;
    //    private TextView mLastSeenTextView;
    private LinearLayout mUserDateSpinnerContainingLinearLayout;
    private TextView mReminderTextView;

    private EditText mDateEditText;
    private EditText mTimeEditText;
    private String mDefaultTimeOptions12H[];
    private String mDefaultTimeOptions24H[];

    private Button mChooseDateButton;
    private Button mChooseTimeButton;
    private ToDoItem mUserToDoItem;
    private FloatingActionButton mToDoSendFloatingActionButton;
    public static final String DATE_FORMAT = "MMM d, yyyy";
    public static final String DATE_FORMAT_MONTH_DAY = "MMM d";
    public static final String DATE_FORMAT_TIME = "H:m";

    private String mUserEnteredText;
    private boolean mUserHasReminder;
    private Toolbar mToolbar;
    private Date mUserReminderDate;
    private int mUserColor;
    private boolean setDateButtonClickedOnce = false;
    private boolean setTimeButtonClickedOnce = false;
    private LinearLayout mContainerLayout;
    private String theme;
    AnalyticsApplication app;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    Calendar cal = Calendar.getInstance();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (AnalyticsApplication) getActivity().getApplication();
//        setContentView(R.layout.new_to_do_layout);
        //Need references to these to change them during light/dark mode
        ImageButton reminderIconImageButton;
        TextView reminderRemindMeTextView;


        theme = getActivity().getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
        if (theme.equals(MainFragment.LIGHTTHEME)) {
            getActivity().setTheme(R.style.CustomStyle_LightTheme);
            Log.d("OskarSchindler", "Light Theme");
        } else {
            getActivity().setTheme(R.style.CustomStyle_DarkTheme);
        }


        //Show an X in place of <-
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if (cross != null) {
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(cross);

        }


        mUserToDoItem = (ToDoItem) getActivity().getIntent().getSerializableExtra(MainFragment.TODOITEM);

        mUserEnteredText = mUserToDoItem.getToDoText();
        mUserHasReminder = mUserToDoItem.hasReminder();
        mUserReminderDate = mUserToDoItem.getToDoDate();
        mUserColor = mUserToDoItem.getTodoColor();



//        if(mUserToDoItem.getLastEdited()==null) {
//            mLastEdited = new Date();
//        }
//        else{
//            mLastEdited = mUserToDoItem.getLastEdited();
//        }


        reminderIconImageButton = (ImageButton) view.findViewById(R.id.userToDoReminderIconImageButton);
        reminderRemindMeTextView = (TextView) view.findViewById(R.id.userToDoRemindMeTextView);
        if (theme.equals(MainFragment.DARKTHEME)) {
            reminderIconImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_alarm_add_white_24dp));
            reminderRemindMeTextView.setTextColor(Color.WHITE);
        }


        mContainerLayout = (LinearLayout) view.findViewById(R.id.todoReminderAndDateContainerLayout);
        mUserDateSpinnerContainingLinearLayout = (LinearLayout) view.findViewById(R.id.toDoEnterDateLinearLayout);
        mToDoTextBodyEditText1 = (EditText) view.findViewById(R.id.userToDoEditText_1);
        mToDoTextBodyEditText2 = (EditText) view.findViewById(R.id.userToDoEditText_2);
        mToDoTextBodyEditText3 = (EditText) view.findViewById(R.id.userToDoEditText_3);
        mToDoTextBodyEditText4 = (EditText) view.findViewById(R.id.userToDoEditText_4);
        mToDoTextBodyEditText5 = (EditText) view.findViewById(R.id.userToDoEditText_5);
        mToDoDateSwitch = (SwitchCompat) view.findViewById(R.id.toDoHasDateSwitchCompat);
//        mLastSeenTextView = (TextView)findViewById(R.id.toDoLastEditedTextView);
        mToDoSendFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.makeToDoFloatingActionButton);
        mReminderTextView = (TextView) view.findViewById(R.id.newToDoDateTimeReminderTextView);


        mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hideKeyboard(mToDoTextBodyEditText);
            }
        });


        if (mUserHasReminder && (mUserReminderDate != null)) {
//            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
            setReminderTextView();
            setEnterDateLayoutVisibleWithAnimations(true);
        }
        if (mUserReminderDate == null) {
            mToDoDateSwitch.setChecked(false);
            mReminderTextView.setVisibility(View.INVISIBLE);
        }

        if(mUserEnteredText!=null){
            String[] splitValues=mUserEnteredText.split("\\^");
            for(int i=0;i<splitValues.length;i++){
                if(i==0){
                    mToDoTextBodyEditText1.setText(splitValues[i]);
                } else if(i==1){
                    mToDoTextBodyEditText2.setText(splitValues[i]);
                }else if(i==2){
                    mToDoTextBodyEditText3.setText(splitValues[i]);
                }else if(i==3){
                    mToDoTextBodyEditText4.setText(splitValues[i]);
                }else if(i==4){
                    mToDoTextBodyEditText5.setText(splitValues[i]);
                }
            }

        }

//        TextInputLayout til = (TextInputLayout)findViewById(R.id.toDoCustomTextInput);
//        til.requestFocus();
//        mToDoTextBodyEditText.requestFocus();
//        mToDoTextBodyEditText.setText(mUserEnteredText);
//        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        mToDoTextBodyEditText.setSelection(mToDoTextBodyEditText.length());


      /*  mToDoTextBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEnteredText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
*/

//        String lastSeen = formatDate(DATE_FORMAT, mLastEdited);
//        mLastSeenTextView.setText(String.format(getResources().getString(R.string.last_edited), lastSeen));

        setEnterDateLayoutVisible(mToDoDateSwitch.isChecked());

        mToDoDateSwitch.setChecked(mUserHasReminder && (mUserReminderDate != null));
        mToDoDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    app.send(this, "Action", "Reminder Set");
                } else {
//                    app.send(this, "Action", "Reminder Removed");

                }

                if (!isChecked) {
                    mUserReminderDate = null;
                }
                mUserHasReminder = isChecked;
                setDateAndTimeEditText();
                setEnterDateLayoutVisibleWithAnimations(isChecked);
//                hideKeyboard(mToDoTextBodyEditText);
            }
        });


        mToDoSendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFileds()) {
//                    mToDoTextBodyEditText.setError(getString(R.string.todo_error));
                    return;
                } else if (mUserReminderDate != null && mUserReminderDate.before(new Date())) {
                    Toast.makeText(getActivity(),"Check Reminder Date",Toast.LENGTH_SHORT).show();
                    app.send(this, "Action", "Date in the Past");
                    makeResult(RESULT_CANCELED);
                } else {
//                    app.send(this, "Action", "Make Todo");
                    makeResult(RESULT_OK);
                    getActivity().finish();
                }
//                hideKeyboard(mToDoTextBodyEditText);
            }
        });
        String[] selectedDate= Utils.lastSelectedDate.split("/");
        if(selectedDate!=null && selectedDate.length>0){
            day=Integer.parseInt(selectedDate[0]);
            month=Integer.parseInt(selectedDate[1]);
            year=Integer.parseInt(selectedDate[2]);
        }
        cal.set(year,  month-1, day);
        mDateEditText = (EditText) view.findViewById(R.id.newTodoDateEditText);
        mTimeEditText = (EditText) view.findViewById(R.id.newTodoTimeEditText);
        mDateEditText.setText(day+"-"+month+"-"+year);
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Date date;
//                hideKeyboard(mToDoTextBodyEditText);
//                if (mUserToDoItem.getToDoDate() != null) {
////                    date = mUserToDoItem.getToDoDate();
//                    date = mUserReminderDate;
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(date);
//                    year = calendar.get(Calendar.YEAR);
//                    month = calendar.get(Calendar.MONTH);
//                    day = calendar.get(Calendar.DAY_OF_MONTH);
//                } else {
//                    date = new Date();
//                }
//
//
//                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddToDoFragment.this, year, month, day);
//                if (theme.equals(MainFragment.DARKTHEME)) {
//                    datePickerDialog.setThemeDark(true);
//                }
//                datePickerDialog.show(getActivity().getFragmentManager(), "DateFragment");


            }
        });


        mTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date;
//                hideKeyboard(mToDoTextBodyEditText);
                if (mUserToDoItem.getToDoDate() != null) {
//                    date = mUserToDoItem.getToDoDate();
                    date = mUserReminderDate;
                } else {
                    date = new Date();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddToDoFragment.this, hour, minute, DateFormat.is24HourFormat(getContext()));
                if (theme.equals(MainFragment.DARKTHEME)) {
                    timePickerDialog.setThemeDark(true);
                }
                timePickerDialog.show(getActivity().getFragmentManager(), "TimeFragment");
            }
        });

//        mDefaultTimeOptions12H = new String[]{"9:00 AM", "12:00 PM", "3:00 PM", "6:00 PM", "9:00 PM", "12:00 AM"};
//        mDefaultTimeOptions24H = new String[]{"9:00", "12:00", "15:00", "18:00", "21:00", "24:00"};
        setDateAndTimeEditText();

//

//        mChooseDateButton = (Button)findViewById(R.id.newToDoChooseDateButton);
//        mChooseTimeButton = (Button)findViewById(R.id.newToDoChooseTimeButton);
//
//        mChooseDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Date date;
//                hideKeyboard(mToDoTextBodyEditText);
//                if(mUserToDoItem.getToDoDate()!=null){
//                    date = mUserToDoItem.getToDoDate();
//                }
//                else{
//                    date = new Date();
//                }
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddToDoActivity.this, year, month, day);
//                if(theme.equals(MainActivity.DARKTHEME)){
//                    datePickerDialog.setThemeDark(true);
//                }
//                datePickerDialog.show(getFragmentManager(), "DateFragment");
//            }
//        });
//
//        mChooseTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Date date;
//                hideKeyboard(mToDoTextBodyEditText);
//                if(mUserToDoItem.getToDoDate()!=null){
//                    date = mUserToDoItem.getToDoDate();
//                }
//                else{
//                    date = new Date();
//                }
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddToDoActivity.this, hour, minute, DateFormat.is24HourFormat(AddToDoActivity.this));
//                if(theme.equals(MainActivity.DARKTHEME)){
//                    timePickerDialog.setThemeDark(true);
//                }
//                timePickerDialog.show(getFragmentManager(), "TimeFragment");
//            }
//        });

    }

    private boolean validateFileds(){
        String finalString="";
        if(mToDoTextBodyEditText1.getText().toString().isEmpty()){
            mToDoTextBodyEditText1.requestFocus();
            Toast.makeText(getActivity(),"First Line Required",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            finalString=mToDoTextBodyEditText1.getText().toString();
            finalString=finalString+"^"+mToDoTextBodyEditText2.getText().toString();
            finalString=finalString+"^"+mToDoTextBodyEditText3.getText().toString();
            finalString=finalString+"^"+mToDoTextBodyEditText4.getText().toString();
            finalString=finalString+"^"+mToDoTextBodyEditText5.getText().toString();
        }
        mUserEnteredText=finalString;
        return true;
    }

    private void setDateAndTimeEditText() {

        if (mUserToDoItem.hasReminder() && mUserReminderDate != null) {
            String userDate = formatDate("d MMM, yyyy", mUserReminderDate);
            String formatToUse;
            if (DateFormat.is24HourFormat(getContext())) {
                formatToUse = "k:mm";
            } else {
                formatToUse = "h:mm a";

            }
            String userTime = formatDate(formatToUse, mUserReminderDate);
            mTimeEditText.setText(userTime);
            mDateEditText.setText(userDate);

        } else {

//            mUserReminderDate = new Date();
            boolean time24 = DateFormat.is24HourFormat(getContext());

            if (time24) {
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
            } else {
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 1);
            }
            cal.set(Calendar.MINUTE, 0);
            mUserReminderDate = cal.getTime();
            mDateEditText.setText(formatDate("dd-MM-yyyy",mUserReminderDate));
            Log.d("OskarSchindler", "Imagined Date: " + mUserReminderDate);
            String timeString;
            if (time24) {
                timeString = formatDate("k:mm", mUserReminderDate);
            } else {
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            mTimeEditText.setText(timeString);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            if(hour<9){
//                timeOption = time24?mDefaultTimeOptions24H[0]:mDefaultTimeOptions12H[0];
//            }
//            else if(hour < 12){
//                timeOption = time24?mDefaultTimeOptions24H[1]:mDefaultTimeOptions12H[1];
//            }
//            else if(hour < 15){
//                timeOption = time24?mDefaultTimeOptions24H[2]:mDefaultTimeOptions12H[2];
//            }
//            else if(hour < 18){
//                timeOption = time24?mDefaultTimeOptions24H[3]:mDefaultTimeOptions12H[3];
//            }
//            else if(hour < 21){
//                timeOption = time24?mDefaultTimeOptions24H[4]:mDefaultTimeOptions12H[4];
//            }
//            else{
//                timeOption = time24?mDefaultTimeOptions24H[5]:mDefaultTimeOptions12H[5];
//            }
//            mTimeEditText.setText(timeOption);
        }
    }

    private String getThemeSet() {
        return getActivity().getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
    }

    public void hideKeyboard(EditText et) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }


    public void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentMonth = calendar.get(Calendar.MONTH);
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);
        this.year = year;
        this.month = month;
        this.day = day;
        if (reminderCalendar.before(calendar)) {
            //    Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

        if (DateFormat.is24HourFormat(getContext())) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else {

            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();
        setReminderTextView();
//        setDateAndTimeEditText();
        setDateEditText();
    }

    public void setTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

//        if(DateFormat.is24HourFormat(this) && hour == 0){
//            //done for 24h time
//                hour = 24;
//        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("OskarSchindler", "Time set: " + hour);
        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();

        setReminderTextView();
//        setDateAndTimeEditText();
        setTimeEditText();
    }

    public void setDateEditText() {
        String dateFormat = "d MMM, yyyy";
        mDateEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void setTimeEditText() {
        String dateFormat;
        if (DateFormat.is24HourFormat(getContext())) {
            dateFormat = "k:mm";
        } else {
            dateFormat = "h:mm a";

        }
        mTimeEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void setReminderTextView() {
        if (mUserReminderDate != null) {
            mReminderTextView.setVisibility(View.VISIBLE);
            if (mUserReminderDate.before(new Date())) {
                Log.d("OskarSchindler", "DATE is " + mUserReminderDate);
                mReminderTextView.setText(getString(R.string.date_error_check_again));
                mReminderTextView.setTextColor(Color.RED);
                return;
            }
            Date date = mUserReminderDate;
            String dateString = formatDate("d MMM, yyyy", date);
            String timeString;
            String amPmString = "";

            if (DateFormat.is24HourFormat(getContext())) {
                timeString = formatDate("k:mm", date);
            } else {
                timeString = formatDate("h:mm", date);
                amPmString = formatDate("a", date);
            }
            String finalString = String.format(getResources().getString(R.string.remind_date_and_time), dateString, timeString, amPmString);
            mReminderTextView.setTextColor(getResources().getColor(R.color.secondary_text));
            mReminderTextView.setText(finalString);
        } else {
            mReminderTextView.setVisibility(View.INVISIBLE);

        }
    }

    public void makeResult(int result) {
        Intent i = new Intent();
        if (mUserEnteredText.length() > 0) {

            String capitalizedString = Character.toUpperCase(mUserEnteredText.charAt(0)) + mUserEnteredText.substring(1);
            mUserToDoItem.setToDoText(capitalizedString);
        } else {
            mUserToDoItem.setToDoText(mUserEnteredText);
        }
//        mUserToDoItem.setLastEdited(mLastEdited);
        if (mUserReminderDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mUserReminderDate);
            calendar.set(Calendar.SECOND, 0);
            mUserReminderDate = calendar.getTime();
        }
        mUserToDoItem.setHasReminder(mUserHasReminder);
        mUserToDoItem.setToDoDate(mUserReminderDate);
        mUserToDoItem.setTodoColor(mUserColor);
        i.putExtra(MainFragment.TODOITEM, mUserToDoItem);
        getActivity().setResult(result, i);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    app.send(this, "Action", "Discard Todo");
                    makeResult(RESULT_CANCELED);
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
//                hideKeyboard(mToDoTextBodyEditText);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String formatDate(String formatString, Date dateToFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        setTime(hour, minute);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        setDate(year, month, day);
    }

    public void setEnterDateLayoutVisible(boolean checked) {
        if (checked) {
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setEnterDateLayoutVisibleWithAnimations(boolean checked) {
        if (checked) {
            setReminderTextView();
            mUserDateSpinnerContainingLinearLayout.animate().alpha(1.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }
            );
        } else {
            mUserDateSpinnerContainingLinearLayout.animate().alpha(0.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }
            );
        }

    }


    @Override
    protected int layoutRes() {
        return R.layout.fragment_add_to_do;
    }

    public static AddToDoFragment newInstance() {
        return new AddToDoFragment();
    }
}
