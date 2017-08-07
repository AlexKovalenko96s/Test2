package com.example.alexkovalenko.test2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Socket socket;

    public static String user = "Not authorized";

    public TextView tv_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_user.setText(user);
    }

    public void login(View v){
        if(v.getId() == R.id.btn_logIn){
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
        }
    }

    public void odessa(View v){
        if(v.getId() == R.id.btn_odessa){
            Intent i = new Intent(MainActivity.this, Odessa.class);
            startActivity(i);

            Odessa.user = user;
        }
    }

    public void signup(View v){
        if(v.getId() == R.id.btn_signUp){
            Intent i = new Intent(MainActivity.this, Signup.class);
            startActivity(i);
        }
    }
}







/*
        Button send = (Button) findViewById(R.id.bSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket("192.168.0.103", 9152);
                            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                            DOS.writeUTF("HELLO_WORLD");
                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        };
                    }
                });
                thread.start();
            }
        });

 */