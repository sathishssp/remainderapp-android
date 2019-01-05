package com.example.krishna.mp3alarm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.krishna.mp3alarm.sharedPreferenceManagermms.SharedPrefManager;

import java.util.ArrayList;
import java.util.Map;

public class AllowedNumbersmms extends AppCompatActivity {
    Button saveButton;
    EditText phoneNumberEditText;
    ListView allowedNumbersListView;
    FloatingActionButton pickContact;
    LinearLayout instructionLinearLayout;

    Map<String, ?> allowedNumbers;
    ArrayList<String> sharedPrefValueIndexes;

    private final int PICK_CONTACT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.krishna.mp3alarm.R.layout.activity_allowed_numbersmms);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        initializeVariables();
        showAllAllowedNumbers();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(phoneNumberEditText.getText())){

                    phoneNumberEditText.setError( "phone number is required!" );

                }else{
                    String number = phoneNumberEditText.getText().toString();
                    SharedPrefManager manager = SharedPrefManager.getInstance(AllowedNumbersmms.this);
                    manager.saveAllowedNumber(number);
                    Toast.makeText(getApplicationContext(), "Number Saved!",
                            Toast.LENGTH_SHORT).show();

                    showAllAllowedNumbers();
                    phoneNumberEditText.setText("");
                }

            }
        });

        allowedNumbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = sharedPrefValueIndexes.get(position);
                phoneNumberEditText.setText(key);
            }
        });

        allowedNumbersListView.setOnItemLongClickListener(new AdapterView
                .OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new
                        AlertDialog.Builder(AllowedNumbersmms.this);
                alertDialogBuilder.setMessage("Are you sure want to delete?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                removePhone(position);
                                showAllAllowedNumbers();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });

        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    private void initializeVariables() {
        saveButton = findViewById(com.example.krishna.mp3alarm.R.id.save_button);
        phoneNumberEditText = findViewById(com.example.krishna.mp3alarm.R.id.phone_number_edit_text);
        allowedNumbersListView = findViewById(com.example.krishna.mp3alarm.R.id.allowed_numbers_list_view);
        pickContact = findViewById(com.example.krishna.mp3alarm.R.id.pick_contact);
        instructionLinearLayout = findViewById(com.example.krishna.mp3alarm.R.id.instruction_linear_layout);
    }

    private void setInstructions() {
        if (allowedNumbers.size() == 0) {
            instructionLinearLayout.setVisibility(View.GONE);
        } else
            instructionLinearLayout.setVisibility(View.VISIBLE);
    }

    private void showAllAllowedNumbers() {
        SharedPrefManager manager = SharedPrefManager.getInstance(this);
        allowedNumbers = manager.getAllAllowedNumbers();
        ArrayList<String> tempAllowedNumbers = new ArrayList<>();

        for (Map.Entry<String, ?> allowedNumber : allowedNumbers.entrySet()) {
            String allowedPersonName = manager.getNameFromNumber(allowedNumber.getKey());
            String tempAllowedNumber;

            if (!TextUtils.isEmpty(allowedPersonName)) {
                tempAllowedNumber = allowedNumber.getKey() + " ("
                        + allowedPersonName + ")";
            } else {
                tempAllowedNumber = allowedNumber.getKey();
            }
            tempAllowedNumbers.add(tempAllowedNumber);
        }

        ArrayAdapter<String> allowedNumbersAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1,
                tempAllowedNumbers);
        allowedNumbersListView.setAdapter(allowedNumbersAdapter);

        // Saving the index number of map to update / delete data
        sharedPrefValueIndexes = new ArrayList<>(allowedNumbers.keySet());
        setInstructions();
    }

    private void removePhone(int position) {
        String key = sharedPrefValueIndexes.get(position);
        SharedPrefManager manager = SharedPrefManager
                .getInstance(AllowedNumbersmms.this);
        manager.removeValue(key);
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
