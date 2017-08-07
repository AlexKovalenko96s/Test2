package com.example.alexkovalenko.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by AlexKovalenko on 05.08.2017.
 */

public class Signup extends AppCompatActivity {

    public EditText et_login;
    public EditText et_password;
    public EditText et_email;
    public EditText et_repeatPassword;

    private String login;
    private String password;
    private String email;
    private String repeatPassword;

    private Socket socket;

    private Thread thread;

    public Button btn_register;

    public TextView tv_errorSignIn;

    private String line = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        et_login = (EditText) findViewById(R.id.et_loginSignUp);
        et_password = (EditText) findViewById(R.id.et_passwordSignUp);
        et_email = (EditText) findViewById(R.id.et_emailSignIn);
        et_repeatPassword = (EditText) findViewById(R.id.ed_repeadPassword);

        btn_register = (Button) findViewById(R.id.btn_register);

        tv_errorSignIn = (TextView) findViewById(R.id.tv_errorSignIn);

    }

    public void enter(View v) {
        if (v.getId() == R.id.btn_register) {
            login = et_login.getText().toString();
            password = et_password.getText().toString();
            email = et_email.getText().toString();
            repeatPassword = et_repeatPassword.getText().toString();

            if (login.contains("&") || login.contains(" ") || login.equals("admin") || login.equals("")) {
                tv_errorSignIn.setVisibility(View.VISIBLE);
                tv_errorSignIn.setText("Please select correct name!");
                return;
            }
            if (email.contains("&") || email.contains(" ") || !email.contains("@") || email.equals("")) {
                tv_errorSignIn.setText("Please select correct email!");
                tv_errorSignIn.setVisibility(View.VISIBLE);
                return;
            }
            if (password.contains("&") || password.contains(" ") || password.equals("admin") || password.equals("")) {
                tv_errorSignIn.setText("Please select correct password!");
                tv_errorSignIn.setVisibility(View.VISIBLE);
                return;
            }
            if (!repeatPassword.equals(password)) {
                tv_errorSignIn.setText("Passwords do not match!");
                tv_errorSignIn.setVisibility(View.VISIBLE);
                return;
            }

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket("192.168.0.103", 9152);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                        out.println("signUp&" + login + "&" + email + "&" + password);
                        out.print("END");

                        String line = in.readLine();

                        if(line.equals("errorLogin")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_errorSignIn.setText("User with this login is already exists!");
                                    tv_errorSignIn.setVisibility(View.VISIBLE);
                                }
                            });

                            socket.close();
                            return;
                        }

                        if(line.equals("errorEmail")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_errorSignIn.setText("User with this email is already exists!");
                                    tv_errorSignIn.setVisibility(View.VISIBLE);
                                }
                            });

                            socket.close();
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Congratulations, now you can log in", Toast.LENGTH_LONG).show();
                            }
                        });

                        socket.close();

                        Intent i = new Intent(Signup.this, MainActivity.class);
                        startActivity(i);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
}