package com.example.alexkovalenko.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by AlexKovalenko on 05.08.2017.
 */

public class Login extends Activity {

    private String user = "";

    private Thread thread;

    private Socket socket;

    public EditText et_login;
    public EditText et_password;

    public Button btn_enterLogIn;

    public TextView tv_error;

    private String line = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_login = (EditText) findViewById(R.id.et_login);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_enterLogIn = (Button) findViewById(R.id.btn_enterLogIn);

        tv_error = (TextView) findViewById(R.id.tv_error);
    }

    public void enter(View v) {
        if (v.getId() == R.id.btn_enterLogIn) {
            if (!et_login.getText().equals("") && !et_password.getText().equals("")) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket("192.168.0.103", 9152);

                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                            user = et_login.getText().toString();

                            out.println("login&" + user + "&" + et_password.getText());
                            out.print("END");

                            line = in.readLine();

                            if (line.equals("0")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_error.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else if (line.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Greetings, my lord!", Toast.LENGTH_LONG).show();
                                    }
                                });

                                MainActivity.user = user;
                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Hello user!", Toast.LENGTH_LONG).show();
                                    }
                                });

                                MainActivity.user = user;
                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);
                            }

                            socket.close();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        ;
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_error.setVisibility(View.VISIBLE);
                    }
                });
            }
            thread.start();
        }
    }
}
