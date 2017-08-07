package com.example.alexkovalenko.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by AlexKovalenko on 05.08.2017.
 */

public class Odessa extends AppCompatActivity{

    public TextView tv_user;

    public static String user = "Not authorized";

    Socket clientSocket;

    ArrayList<String> list = new ArrayList<>();

    String[] names;

    String strFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odessa);

        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_user.setText(user);
    }


    public void mustSee(View v){
        if(v.getId() == R.id.btn_mustSee){

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        clientSocket = new Socket("192.168.0.103", 9152);

                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

                        out.println("mustSee&odessa");
                        out.print("END");

                        strFromServer = in.readLine();

                        while (true) {
                            if (strFromServer.contains("&")) {
                                list.add(strFromServer.substring(0, strFromServer.indexOf("&")));
                                strFromServer = strFromServer.substring(strFromServer.indexOf("&") + 1);
                            } else {
                                list.add(strFromServer);

                                names = new String[list.size()];
                                for (int i = 0; i != list.size(); i++) {
                                    names[i] = list.get(i);
                                }

                                clientSocket.close();

                                MustSee.names = names;
                                MustSee.user = user;

                                Intent i = new Intent(Odessa.this, MustSee.class);
                                startActivity(i);

                                break;
                            }

                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
}