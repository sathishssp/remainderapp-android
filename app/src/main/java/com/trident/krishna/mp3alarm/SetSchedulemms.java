package com.trident.krishna.mp3alarm;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trident.krishna.mp3alarm.databasemms.DatabaseHelper;
import com.trident.krishna.mp3alarm.databasemms.model.MyAlarmsDBModel;
import com.trident.krishna.mp3alarm.smsManagermms.SMSManager;

public class SetSchedulemms extends AppCompatActivity {
    EditText phoneNumberEditText;
    Button sendButton;
    FloatingActionButton pickContact;

    SMSManager smsManager;
    private final int PICK_CONTACT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trident.krishna.mp3alarm.R.layout.activity_set_schedulemms);

        phoneNumberEditText = findViewById(com.trident.krishna.mp3alarm.R.id.phone_number_edit_text);
        pickContact = findViewById(com.trident.krishna.mp3alarm.R.id.pick_contact);
        sendButton = findViewById(com.trident.krishna.mp3alarm.R.id.send_button);
        smsManager = (SMSManager) getIntent().
                getSerializableExtra(Constantsmms.SMS_MANAGER);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = phoneNumberEditText.getText().toString();
               // checkPermission();
                if (!isValidNumber(phoneNumber)) {
                    Toast.makeText(SetSchedulemms.this,
                            "Please enter valid number", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog
                        .Builder(SetSchedulemms.this);
                alertDialogBuilder.setMessage("Are you sure want"+ "to set schedule?");

                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                smsManager.setReceiverPhoneNumber(phoneNumber);
                                smsManager.sendSMS();
                                saveInfoToDatabase();
                                Toast.makeText(SetSchedulemms.this, "Schedule is set",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SetSchedulemms.this,
                                        MySchedulesmms.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPickContactPermission();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    private boolean isValidNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber))
            return false;
        else if (!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber))
            return false;
        return true;

    }

    private void saveInfoToDatabase() {
        MyAlarmsDBModel dbModel = new MyAlarmsDBModel();
        dbModel.setMessage(smsManager.getMessage());
        dbModel.setPhone(smsManager.getReceiverPhoneNumber());
        dbModel.setImage(smsManager.getImage());
        dbModel.setSound(smsManager.getSound());
        dbModel.setPhoneStatus(smsManager.getPhoneStatus());
        dbModel.setTimestamp(smsManager.getTimestamp());
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.insertMyAlarm(dbModel);
    }

    private void checkPermission() {
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            int REQUEST_SMS_PERMISSION = 1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_PERMISSION);
        }*/
    }

    private void checkPickContactPermission() {
        if (ContextCompat.checkSelfPermission(SetSchedulemms.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(SetSchedulemms.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    10);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null,
                            null, null, null);
                    if (c.moveToFirst()) {
                        String contactNumber = c.getString(
                                c.getColumnIndexOrThrow(ContactsContract.
                                        CommonDataKinds.Phone.NUMBER));
                        phoneNumberEditText.setText(contactNumber);
                    }
                }
                break;
        }
    }
}
