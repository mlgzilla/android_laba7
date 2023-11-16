package com.example.laba2;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ThreadTask {
    Handler thr_handler;
    final Message message = Message.obtain();
    DatabaseHandler db;

    public ThreadTask(Handler thr_handler, Context context) {
        this.thr_handler = thr_handler;
        this.db = new DatabaseHandler(context);
    }

    public void loginUser(String login, String pass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer userId = db.loginUser(login, pass);
                message.sendingUid = 1;
                message.obj = userId;
                thr_handler.sendMessage(message);
            }
        }).start();
    }

    public void regUser(String login, String pass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer userId = db.authUser(login);
                if (userId != null){
                    message.obj = userId;
                }
                else {
                    message.obj = null;
                    User user = new User();
                    user.setLogin(login);
                    user.setPass(pass);
                    db.addUser(user);
                }
                message.sendingUid = 2;
                thr_handler.sendMessage(message);
            }
        }).start();
    }

    public void updatePass(String login, String pass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!pass.equals("")){
                    db.updatePass(db.findByLogin(login), pass);
                    message.obj = "Password was updated";
                }
                else
                    message.obj = "Pass cant be empty";

                message.sendingUid = 1;
                thr_handler.sendMessage(message);
            }
        }).start();
    }

    public void removeUser(String login) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.removeUser(login);
                message.sendingUid = 2;
                thr_handler.sendMessage(message);
            }
        }).start();
    }

}
