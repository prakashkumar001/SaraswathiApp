package com.saraswathi.banjagam.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.adapter.CategoryListAdapter;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.database.Categories;

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
    LinearLayout layout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        global=(GlobalClass)getApplicationContext();
        productListView = (RecyclerView) findViewById(R.id.productList);
        cartcount=(TextView)findViewById(R.id.cartcount);
        cartRelativeLayout=(RelativeLayout)findViewById(R.id.cartRelativeLayout);
        layout=(LinearLayout)findViewById(R.id.layout);










        cartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent i=new Intent(DashBoard.this,CartPage.class);
                startActivity(i);*/
                //showOrderDialog();

            }
        });






        if(global.cartList.size()>0)
        {
            cartcount.setText(String.valueOf(global.cartList.size()));
        }





    }


    @Override
    protected void onResume() {
        super.onResume();

        getCategoryList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(DashBoard.this, Home.class);
        startActivity(i);
        finish();
    }







    public void getCategoryList() {
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


                    String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "product/categories";
                    WSUtils utils = new WSUtils();



                    response = utils.getResultFromHttpRequest(requestURL, "GET",new HashMap<String, String>());

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
                        JSONObject object = new JSONObject(o);
                        JSONArray array=object.getJSONArray("payload");

                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject data=array.getJSONObject(i);
                            String categoryId=data.getString("categoryId");
                            String categoryName=data.getString("categoryName");
                            String categoryUid=data.getString("categoryUid");
                            String active=data.getString("active");

                            Categories categories=new Categories();
                            categories.categoryId=(Long.parseLong(categoryId));
                            categories.categoryName=categoryName;
                            categories.categoryUid=categoryUid;
                            categories.active=active;

                            getHelper().getDaoSession().insertOrReplace(categories);

                        }

                        adapter=new CategoryListAdapter(DashBoard.this,new ArrayList<Categories>());
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


