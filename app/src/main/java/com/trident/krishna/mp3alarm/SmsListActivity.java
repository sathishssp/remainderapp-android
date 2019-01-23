package com.trident.krishna.mp3alarm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SmsListActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 1;
    ListView smsListView;
    Button btnAdd;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            startActivityForResult(new Intent(this, SmsSchedulerPreferenceActivity.class), 1);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE || resultCode == 0) {
            return;
        }
        int messageId;
        switch (resultCode) {
            case AddSmsActivity.RESULT_SCHEDULED:
                messageId = R.string.successfully_scheduled;
                break;
            case AddSmsActivity.RESULT_UNSCHEDULED:
                messageId = R.string.successfully_unscheduled;
                break;
            default:
                messageId = R.string.error_generic;
                break;
        }
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setListAdapter(getSmsListAdapter());
        if(smsListView!=null)
        smsListView.setAdapter(getSmsListAdapter());
    }

    @Override
    protected void onPause() {
        super.onPause();
//        ((SimpleCursorAdapter) getListAdapter()).getCursor().close();
        DbHelper.closeDbHelper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ((SimpleCursorAdapter) getListAdapter()).getCursor().close();
        DbHelper.closeDbHelper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_list_activity);
        smsListView=findViewById(R.id.smsList);
        btnAdd=findViewById(R.id.btn_add_sms);

        smsListView.setAdapter(getSmsListAdapter());

//        View headerView = getLayoutInflater().inflate(R.layout.item_add, getListView(), false);
//        headerView.setClickable(true);
//        getListView().addFooterView(headerView);
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddSmsActivity.class);
                intent.putExtra(AddSmsActivity.INTENT_SMS_ID, id);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNextActivity(view);
            }
        });


    }

    public void gotoNextActivity(View view) {
        startActivityForResult(new Intent(getApplicationContext(), AddSmsActivity.class), REQUEST_CODE);
    }

    private SimpleCursorAdapter getSmsListAdapter() {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                DbHelper.getDbHelper(this).getCursor(),
                new String[] { DbHelper.COLUMN_MESSAGE, DbHelper.COLUMN_RECIPIENT_NAME },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                TextView textView = (TextView) view;
                if (textView.getId() == android.R.id.text2) {
                    textView.setText(getFormattedSmsInfo(cursor));
                    return true;
                }
                return false;
            }
        });
        return adapter;
    }

    private String getFormattedSmsInfo(Cursor cursor) {
        String recipient, recipientName, recipientNumber, status, datetime;
        recipientName = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_RECIPIENT_NAME));
        recipientNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_RECIPIENT_NUMBER));
        recipient = recipientName.length() > 0 ? recipientName : recipientNumber;
        switch (cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_STATUS))) {
            case (SmsModel.STATUS_PENDING):
                status = getString(R.string.list_status_pending);
                break;
            case (SmsModel.STATUS_SENT):
                status = getString(R.string.list_status_sent);
                break;
            case (SmsModel.STATUS_DELIVERED):
                status = getString(R.string.list_status_delivered);
                break;
            default:
                status = getString(R.string.list_status_failed);
        }
        datetime = DateUtils.formatDateTime(
            this,
            cursor.getLong(cursor.getColumnIndex(DbHelper.COLUMN_TIMESTAMP_SCHEDULED)),
            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
        );
        return getString(R.string.list_sms_info_template, status, recipient, datetime);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(SmsListActivity.this, Reminderpage.class);
        overridePendingTransition(R.anim.rotate, R.anim.rotate);
        startActivity(intent);
        finish();

    }
}
