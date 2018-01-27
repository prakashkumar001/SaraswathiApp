package com.saraswathi.banjagam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.interfaces.NetworkConnection;


/**
 * Created by Prakash on 9/14/2017.
 */

public class Splash extends AppCompatActivity implements NetworkConnection {
    final int SPLASH_DISPLAY_TIME = 2000;
    GlobalClass global;
    LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        global=(GlobalClass)getApplicationContext();



        new Handler().postDelayed(new Runnable() {
            public void run() {

                Splash.this.finish();
                overridePendingTransition(R.anim.fadeinact,
                        R.anim.fadeoutact);


                    Intent mainIntent = new Intent(
                            Splash.this,
                            Login.class);

                    Splash.this.startActivity(mainIntent);








            }
        }, SPLASH_DISPLAY_TIME);
    }

    @Override
    public void isInternetOn(String message) {

    }



}
