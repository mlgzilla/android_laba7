package com.example.laba2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    Context context = this;

    String userLogin;

    Button submitButton;
    Button deleteButton;
    Button backButton;
    EditText passwdText;

    Intent authIntent;
    Intent listIntent;


    final Looper looper = Looper.getMainLooper();

    final Handler handler = new Handler(looper) {
        @Override
        public void handleMessage(Message msg) {

            if (msg.sendingUid == 1) {
                Toast.makeText(SettingsActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                submitButton.setEnabled(true);
            }

            if (msg.sendingUid == 2) {
                startActivity(authIntent);
                deleteButton.setEnabled(true);
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.settings_activity);

        Bundle arguments = getIntent().getExtras();
        userLogin = arguments.get("userLogin").toString();

        submitButton = findViewById(R.id.submitButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        passwdText = findViewById(R.id.editTextPassword);
        authIntent = new Intent(this, AuthActivity.class);
        listIntent = new Intent(this, ListActivity.class);

        Toast myToast = new Toast(this);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);
                new ThreadTask(handler, context).updatePass(userLogin, passwdText.getText().toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButton.setEnabled(false);
                new ThreadTask(handler, context).removeUser(userLogin);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listIntent.putExtra("userLogin", userLogin);
                startActivity(listIntent);
            }
        });
    }
}
