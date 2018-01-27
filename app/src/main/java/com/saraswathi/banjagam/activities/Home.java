package com.saraswathi.banjagam.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.database.Categories;
import com.saraswathi.banjagam.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.saraswathi.banjagam.helper.Helper.getHelper;


/**
 * Created by Prakash on 9/25/2017.
 */

public class Home extends AppCompatActivity {
    LinearLayout breakfast,lunch,dinner;
    int backPressedCount = 0;
    GlobalClass global;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        global=(GlobalClass)getApplicationContext();
        breakfast=(LinearLayout)findViewById(R.id.breakfast);
        lunch=(LinearLayout)findViewById(R.id.lunch);
        dinner=(LinearLayout)findViewById(R.id.dinner);


        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this, DashBoard.class);
                startActivity(i);
                finish();
            }
        });


        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this, DashBoard.class);
                startActivity(i);
                finish();
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this, DashBoard.class);
                startActivity(i);
                finish();

            }
        });


    }


    @Override
    public void onBackPressed() {


        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (backPressedCount == 1) {
                ActivityCompat.finishAffinity(Home.this);
            } else {
                backPressedCount++;

                new Thread() {
                    @Override
                    public void run() {
                        //super.run();
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            backPressedCount = 0;
                        }
                    }
                }.start();
            }
        } else {
            super.onBackPressed();
        }


    }









}
