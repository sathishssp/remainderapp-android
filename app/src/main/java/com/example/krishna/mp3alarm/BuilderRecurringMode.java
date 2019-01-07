package com.example.krishna.mp3alarm;

import android.icu.util.Calendar;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.krishna.mp3alarm.Utility.Utils;

public class BuilderRecurringMode extends BuilderSpinner {

    static private final String[] modes = new String[] {
        CalendarResolver.RECURRING_NO,
        CalendarResolver.RECURRING_DAILY,
        CalendarResolver.RECURRING_WEEKLY,
        CalendarResolver.RECURRING_MONTHLY,
        CalendarResolver.RECURRING_YEARLY,
    };

    private Spinner recurringDayView;
    private Spinner recurringMonthView;
    private DatePicker dateView;

    public BuilderRecurringMode setRecurringDayView(Spinner recurringDayView) {
        this.recurringDayView = recurringDayView;
        return this;
    }

    public BuilderRecurringMode setRecurringMonthView(Spinner recurringMonthView) {
        this.recurringMonthView = recurringMonthView;
        return this;
    }

    public BuilderRecurringMode setDateView(final DatePicker dateView) {
        this.dateView = dateView;
        int dayOfMonth = 0;
        int month = 0;
        int year =0;
        String[] selectedDate= Utils.lastSelectedDate.split("/");
        if(selectedDate!=null && selectedDate.length>0){
            dayOfMonth=Integer.parseInt(selectedDate[0]);
            month=Integer.parseInt(selectedDate[1]);
            year=Integer.parseInt(selectedDate[2]);
        }
        Calendar cal = Calendar.getInstance();
        cal.set(year,  month, dayOfMonth);
        final int xxday = cal.get(Calendar.DATE);
        final int xxmonth = cal.get(Calendar.MONTH);
        final int xxyear = cal.get(Calendar.YEAR);

        dateView.init(xxyear, xxmonth, xxday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dateView.updateDate(xxyear,xxmonth,xxday);
            }
        },350);

        return this;
    }

    @Override
    public Builder setActivity(AddSmsActivity activity) {
        values.add(activity.getString(R.string.form_recurring_mode_no));
     //   values.add(activity.getString(R.string.form_recurring_mode_daily));
     //   values.add(activity.getString(R.string.form_recurring_mode_weekly));
     //   values.add(activity.getString(R.string.form_recurring_mode_monthly));
     //   values.add(activity.getString(R.string.form_recurring_mode_yearly));
        return super.setActivity(activity);
    }

    public BuilderRecurringMode() {
        for (int i = 0; i < modes.length; i++) {
            keys.put(modes[i], i);
        }
    }

    @Override
    public View build() {
        refreshDependants();
        return super.build();
    }

    @Override
    protected boolean shouldBeVisible() {
        return true;
    }

    @Override
    protected void onAdapterItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sms.setRecurringMode(modes[position]);
        new CalendarResolver().setCalendar(sms.getCalendar()).setRecurringMode(sms.getRecurringMode()).reset().advance();
        refreshDependants();
    }

    @Override
    protected int getSelection() {
        return keys.get(sms.getRecurringMode());
    }

    private void refreshDependants() {
        new BuilderRecurringDay().setActivity(activity).setSms(sms).setView(recurringDayView).build();
        new BuilderRecurringMonth().setActivity(activity).setSms(sms).setView(recurringMonthView).build();
        if(dateView!=null)
        dateView.setVisibility(CalendarResolver.RECURRING_NO.equals(sms.getRecurringMode()) ? View.VISIBLE : View.GONE);
    }
}
