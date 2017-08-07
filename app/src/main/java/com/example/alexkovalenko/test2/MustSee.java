package com.example.alexkovalenko.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;


/**
 * Created by AlexKovalenko on 05.08.2017.
 */

public class MustSee extends AppCompatActivity {

    public static String[] names;

    public TextView tv_user;

    public ListView lv_mustSee;

    public static String user;

    public byte[] imgByte = null;

    private Socket socket;

    public Thread thread;

    private String line = "";

    public String name;
    public String location;
    public String email;
    public String number;
    public String like;
    public String greenOrNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mustsee);

        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_user.setText(user);

        lv_mustSee = (ListView) findViewById(R.id.lv_mustSee);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MustSee.this, R.layout.list, R.id.textview, names);

        lv_mustSee.setAdapter(adapter);

        lv_mustSee.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final String text = lv_mustSee.getItemAtPosition(position).toString().trim();

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket("192.168.0.103", 9152);

                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                            out.println("mustSeeItem&" + text + "&mustSee&" + user);
                            out.print("END");

                            line = in.readLine();


                            name = line.substring(0, line.indexOf("&"));
                            line = line.substring(line.indexOf("&") + 1);
                            location = line.substring(0, line.indexOf("&"));
                            line = line.substring(line.indexOf("&") + 1);
                            number = line.substring(0, line.indexOf("&"));
                            line = line.substring(line.indexOf("&") + 1);
                            email = line.substring(0, line.indexOf("&"));
                            line = line.substring(line.indexOf("&") + 1);
                            like = line.substring(0, line.indexOf("&"));
                            line = line.substring(line.indexOf("&") + 1);
                            greenOrNo = line.substring(0, line.indexOf("&"));
                            line = line.substring(line.indexOf("&") + 1);

                            byte[] imgBytes = line.getBytes("UTF-8");

                            byte[] valueDecoded= android.util.Base64.decode(imgBytes, android.util.Base64.DEFAULT);

                            ItemMustSee.name = name;
                            ItemMustSee.email = email;
                            ItemMustSee.number = number;
                            ItemMustSee.location = location;
                            ItemMustSee.like = like;
                            ItemMustSee.greenOrNo = greenOrNo;
                            ItemMustSee.imgBytes = valueDecoded;

                            ItemMustSee.user = user;

//                            byte[] imgByte = line.getBytes("UTF-8");
//                            Bitmap bmp = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
//                            ItemMustSee.imgByte = imgByte;

                            Intent i = new Intent(MustSee.this, ItemMustSee.class);
                            startActivity(i);

                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}
