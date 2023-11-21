package com.example.laba2;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    Context context = this;

    Button submitButton;
    Button regButton;
    EditText loginText;
    EditText passwdText;

    Intent openIntent;

    final Looper looper = Looper.getMainLooper();

    final Handler handler = new Handler(looper) {
        @Override
        public void handleMessage(Message msg) {

            if (msg.sendingUid == 1) {
                if ((Integer)msg.obj != null) {
                    openIntent.putExtra("userLogin", loginText.getText().toString());
                    startActivity(openIntent);
                } else
                    Toast.makeText(AuthActivity.this, "Wrong login or password, try again.", Toast.LENGTH_LONG).show();

                submitButton.setEnabled(true);
            }

            if (msg.sendingUid == 2) {
                if ((Integer)msg.obj == null) {
                    Toast.makeText(AuthActivity.this, "User was registered.", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(AuthActivity.this, "User with this login already exists.", Toast.LENGTH_LONG).show();

                regButton.setEnabled(true);
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.auth_activity);
        openIntent = new Intent(this, ListActivity.class);

        submitButton = findViewById(R.id.submitButton);
        regButton = findViewById(R.id.regButton);
        loginText = findViewById(R.id.editTextLogin);
        passwdText = findViewById(R.id.editTextPassword);

        Toast myToast = new Toast(this);
        ThreadTask threadTask = new ThreadTask(handler, context);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);

                ArrayList<String> list = new ArrayList<>();
                list.add(loginText.getText().toString());
                list.add(passwdText.getText().toString());

                Message message = Message.obtain();
                message.obj = list;
                message.sendingUid = 1;
                threadTask.handler.sendMessage(message);
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regButton.setEnabled(false);

                ArrayList<String> list = new ArrayList<>();
                list.add(loginText.getText().toString());
                list.add(passwdText.getText().toString());

                Message message = Message.obtain();
                message.obj = list;
                message.sendingUid = 2;
                threadTask.handler.sendMessage(message);
            }
        });
    }
}
