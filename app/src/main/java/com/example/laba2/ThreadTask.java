package com.example.laba2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;

public class ThreadTask {
    Handler targetHandler;
    final Message targetMessage = Message.obtain();
    DatabaseHandler db;

    final Looper looper = Looper.getMainLooper();

    final Handler handler = new Handler(looper) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.sendingUid){
                case 1:{
                    ArrayList<String> list = (ArrayList<String>) msg.obj;
                    loginUser(list.get(0), list.get(1));
                    break;
                }
                case 2:{
                    ArrayList<String> list = (ArrayList<String>) msg.obj;
                    regUser(list.get(0), list.get(1));
                    break;
                }
                case 3:{
                    ArrayList<String> list = (ArrayList<String>) msg.obj;
                    updatePass(list.get(0), list.get(1));
                    break;
                }
                case 4: {
                    removeUser((String) msg.obj);
                    break;
                }
            }
        }
    };

    public ThreadTask(Handler targetHandler, Context context) {
        this.targetHandler = targetHandler;
        this.db = new DatabaseHandler(context);
    }

    public void loginUser(String login, String pass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer userId = db.loginUser(login, pass);
                targetMessage.sendingUid = 1;
                targetMessage.obj = userId;
                targetHandler.sendMessage(targetMessage);
            }
        }).start();
    }

    public void regUser(String login, String pass) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer userId = db.authUser(login);
                if (userId != null) {
                    targetMessage.obj = userId;
                } else {
                    targetMessage.obj = null;
                    User user = new User();
                    user.setLogin(login);
                    user.setPass(pass);
                    db.addUser(user);
                }
                targetMessage.sendingUid = 2;
                targetHandler.sendMessage(targetMessage);
            }
        }).start();
    }

    public void updatePass(String login, String pass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!pass.equals("")) {
                    db.updatePass(db.findByLogin(login), pass);
                    targetMessage.obj = "Password was updated";
                } else targetMessage.obj = "Pass cant be empty";

                targetMessage.sendingUid = 1;
                targetHandler.sendMessage(targetMessage);
            }
        }).start();
    }

    public void removeUser(String login) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.removeUser(login);
                targetMessage.sendingUid = 2;
                targetHandler.sendMessage(targetMessage);
            }
        }).start();
    }

}
