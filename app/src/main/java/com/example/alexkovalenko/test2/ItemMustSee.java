package com.example.alexkovalenko.test2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
 * Created by AlexKovalenko on 06.08.2017.
 */

public class ItemMustSee extends Activity {

    public ImageView im_mustSeeItem;

    private Socket socket;

    public Thread thread;

    public static String user = "Not authorized";

    public static String name;
    public static String location;
    public static String number;
    public static String email;
    public static String like;
    public static String greenOrNo;

    public static byte[] imgBytes;

    private String line = "";

    public Button btn_like;

    public TextView tv_mustSeeItemAddress;
    public TextView tv_nameMustSee;
    public TextView tv_mustSeeItemNumber;
    public TextView tv_mustSeeItemEmail;
    public TextView tv_user;

    public ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_mustsee);

        tv_mustSeeItemAddress = (TextView) findViewById(R.id.tv_mustSeeItemAddress);
        tv_nameMustSee = (TextView) findViewById(R.id.tv_nameMustSee);
        tv_mustSeeItemNumber = (TextView) findViewById(R.id.tv_mustSeeItemNumber);
        tv_mustSeeItemEmail = (TextView) findViewById(R.id.tv_mustSeeItemEmail);
        tv_user = (TextView) findViewById(R.id.tv_user);
        btn_like = (Button) findViewById(R.id.btn_like);
        image = (ImageView) findViewById(R.id.im_mustSeeItem);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_user.setText(user);
                tv_nameMustSee.setText(name);
                tv_mustSeeItemAddress.setText(location);
                tv_mustSeeItemEmail.setText(email);
                tv_mustSeeItemNumber.setText(number);
                btn_like.setText("Like: " + like);

                if (greenOrNo.equals("like")) {
                    btn_like.setBackgroundColor(Color.GREEN);
                }

                Bitmap bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
            }
        });

//        im_mustSeeItem = (ImageView) findViewById(R.id.im_mustSeeItem);
//        Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
//        im_mustSeeItem.setImageBitmap(bmp);
    }

    public void like(View v) {
        if ((v.getId() == R.id.btn_like)) {
            if (!user.equals("Not authorized")) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket("192.168.0.103", 9152);

                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                            out.println("like&" + "mustSee" + "&" + name + "&" + user);
                            out.print("END");

                            line = in.readLine();

                            final String count = line.substring(0, line.indexOf("&"));
                            line = line.substring(line.indexOf("&") + 1);
                            final String like = line;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_like.setText("Like = " + count);

                                    if (like.equals("like")) {
                                        btn_like.setBackgroundColor(Color.GREEN);
                                    } else {
                                        btn_like.setBackgroundColor(Color.rgb(214, 215, 215));
                                    }
                                }
                            });


                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.start();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "First you must be authorized!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
