package com.example.krishna.mp3alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class contact extends AppCompatActivity {

    EditText editname,editmessage;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        editname=(EditText)findViewById(R.id.edit);
        editmessage=(EditText)findViewById(R.id.editone);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@uremindme.app"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Trident Application");
                i.putExtra(Intent.EXTRA_TEXT, "Name : "+editname.getText()+"\nFeedback : "+editmessage.getText());
                try {
                    startActivity(Intent.createChooser(i, "feedback sent"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed () {

        super.onBackPressed();
        Intent intent = new Intent(contact.this, com.example.krishna.mp3alarm.active.MainActivity.class);
        startActivity(intent);
        finish();
    }
}
