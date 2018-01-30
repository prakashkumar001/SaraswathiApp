package com.saraswathi.banjagam.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.adapter.CategoryListAdapter;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.database.Categories;

import com.saraswathi.banjagam.database.FoodType;
import com.saraswathi.banjagam.utils.WSUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.saraswathi.banjagam.helper.Helper.getHelper;


/**
 * Created by Prakash on 9/19/2017.
 */

public class DashBoard extends AppCompatActivity {
    public static RecyclerView productListView;
    public static CategoryListAdapter adapter;
    public static TextView cartcount;
    RelativeLayout cartRelativeLayout;
    GlobalClass global;
    public static  List<Categories> categoriesList;
    public static  List<FoodType> foodType;
    LinearLayout layout;
    RadioGroup radioGroup;
    int backPressedCount = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        global = (GlobalClass) getApplicationContext();
        productListView = (RecyclerView) findViewById(R.id.productList);
        cartcount = (TextView) findViewById(R.id.cartcount);
        cartRelativeLayout = (RelativeLayout) findViewById(R.id.cartRelativeLayout);
        layout = (LinearLayout) findViewById(R.id.layout);
        radioGroup = (RadioGroup) findViewById(R.id.rg_header);

        RadioGroup.LayoutParams rprms;


        if (getHelper().getFoodType() != null) {
            foodType = new ArrayList<>();
            foodType = getHelper().getFoodType();

            for (int i = 0; i < foodType.size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(foodType.get(i).getFoodType());
                radioButton.setTextSize(16);
                radioButton.setPadding(5, 0, 0, 5);
                radioButton.setChecked(i == 0);
                radioButton.setId(i);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTextColor(getResources().getColorStateList(R.color.rbtn_textcolor_selector));
                radioButton.setButtonDrawable(null);
                radioButton.setBackgroundResource(R.drawable.radio_selector_circle);
                rprms = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                radioGroup.addView(radioButton, rprms);


            }


            cartRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                /*Intent i=new Intent(DashBoard.this,CartPage.class);
                startActivity(i);*/
                    //showOrderDialog();

                }
            });


            if (global.cartList.size() > 0) {
                cartcount.setText(String.valueOf(global.cartList.size()));
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCategoryList(String.valueOf(1));


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                    int id=i;

                    getCategoryList(String.valueOf(id+1));



                }
            });
        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backPressedCount == 1) {
            ActivityCompat.finishAffinity(DashBoard.this);
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

    }







    public void getCategoryList(final String id) {
        class CategoryServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(DashBoard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "subcategory";
                    WSUtils utils = new WSUtils();
                    HashMap<String,String> data=new HashMap<>();
                    data.put("main_cattid",id);



                    response = utils.getResultFromHttpRequest(requestURL, "POST",data);

                    System.out.println("SERVER REPLIED:" + response);
                    //{"status":"success","message":"Registration Successful","result":[],"statusCode":200}
                    // {"status":"success","message":"Logged in Successfully","result":{"statusCode":4},"statusCode":200}
                } catch (Exception ex) {
                    Log.i("ERROR", "ERROR" + ex.toString());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String o) {



                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (o == null ) {

                }else {


                    try {

                        JSONArray array=new JSONArray(o);

                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject data=array.getJSONObject(i);
                            String categoryId=data.getString("id");
                            String categoryName=data.getString("sub_categoryname");
                            String categoryTypeId=data.getString("main_catid");
                            String active=data.getString("status");

                            Categories categories=new Categories();
                            categories.categoryId=(Long.parseLong(categoryId));
                            categories.categoryUid=categoryId;
                            categories.categoryTypeId=(categoryTypeId);
                            categories.categoryName=categoryName;
                            categories.active=active;

                            getHelper().getDaoSession().insertOrReplace(categories);

                        }

                        adapter=new CategoryListAdapter(DashBoard.this,getHelper().getCategoryItems());
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                        productListView.setLayoutManager(layoutManager);
                        productListView.setItemAnimator(new DefaultItemAnimator());
                        productListView.setNestedScrollingEnabled(false);
                        productListView.setAdapter(adapter);




                    } catch (JSONException e) {
                        e.printStackTrace();




                    }
                }









            }
        }
        new CategoryServer().execute();
    }


}


