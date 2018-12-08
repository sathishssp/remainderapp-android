package com.example.krishna.mp3alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Contactus extends AppCompatActivity {
    EditText edit, edit1;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        edit = (EditText) findViewById(R.id.editname);
        edit1 = (EditText) findViewById(R.id.editmessage);
        bt = (Button) findViewById(R.id.btn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@tridenttechnominds.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Trident Application");
                i.putExtra(Intent.EXTRA_TEXT, "Name : "+edit.getText()+"\nFeedback : "+edit1.getText());
                try {
                    startActivity(Intent.createChooser(i, "feedback sent"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }*/

            }
            });
        }

        @Override
        public void onBackPressed () {

            super.onBackPressed();
            Intent intent = new Intent(Contactus.this, com.example.krishna.mp3alarm.active.MainActivity.class);
            startActivity(intent);
            Contactus.this.finish();
        }
    }

