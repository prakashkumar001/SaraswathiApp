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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.adapter.CategoryListAdapter;
import com.saraswathi.banjagam.adapter.ProductListAdapter;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.database.Categories;
import com.saraswathi.banjagam.database.Product;
import com.saraswathi.banjagam.model.PrintError;
import com.saraswathi.banjagam.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.saraswathi.banjagam.helper.Helper.getHelper;

/**
 * Created by Prakash on 1/29/2018.
 */

public class ProductDashboard extends AppCompatActivity {
    public static RecyclerView productListView;
    public static ProductListAdapter adapter;
    public static List<Product> productList;
    public static TextView cartcount;
    RelativeLayout cartRelativeLayout;
    GlobalClass global;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_dashboard);
        global=(GlobalClass)getApplicationContext();
        productListView = (RecyclerView) findViewById(R.id.productList);
        cartcount=(TextView)findViewById(R.id.cartcount);
        cartRelativeLayout=(RelativeLayout)findViewById(R.id.cartRelativeLayout);


        cartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent i=new Intent(DashBoard.this,CartPage.class);
                startActivity(i);*/
               // showOrderDialog();

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

        Intent i=getIntent();
        String subid=i.getStringExtra("sub_id");
        getProductList(subid);
    }

    public void getProductList(final String subcategoryId) {
        class ProductFromServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(ProductDashboard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "menuitem";
                    WSUtils utils = new WSUtils();
                    HashMap<String,String> data=new HashMap<>();
                    data.put("sub_catgoryid",subcategoryId);



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
                            String id=data.getString("id");
                            String categoryName=data.getString("menuname");
                            String subcategoryId=data.getString("sub_catgoryid");
                            String active=data.getString("status");
                            String price=data.getString("price");

                            Product categories=new Product();
                            categories.productId=(Long.parseLong(id));
                            categories.categoryUid=subcategoryId;
                            categories.price=(price);
                            categories.productName=categoryName;
                            categories.active=active;

                            getHelper().getDaoSession().insertOrReplace(categories);

                        }

                        adapter=new ProductListAdapter(ProductDashboard.this,getHelper().getProductItems(subcategoryId));
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
        new ProductFromServer().execute();
    }
}
