package com.saraswathi.banjagam.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.adapter.CartAdapter;
import com.saraswathi.banjagam.adapter.ProductListAdapter;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.database.Product;
import com.saraswathi.banjagam.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static TextView totalprice,subtotal;
    public  static  RecyclerView cartview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_dashboard);
        global=(GlobalClass)getApplicationContext();
        productListView = (RecyclerView) findViewById(R.id.productList);
        cartcount=(TextView)findViewById(R.id.cartcount);
        cartRelativeLayout=(RelativeLayout)findViewById(R.id.cartRelativeLayout);







    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent i=getIntent();
        String subid=i.getStringExtra("sub_id");
        getProductList(subid);


        cartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showdialog();
                // showOrderDialog();

            }
        });






        if(global.cartList.size()>0)
        {
            cartcount.setText(String.valueOf(global.cartList.size()));
        }
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
                            String productImage=data.getString("books_url");

                            Product categories=new Product();
                            categories.productId=(Long.parseLong(id));
                            categories.categoryUid=subcategoryId;
                            categories.price=price;
                            categories.totalprice=price;
                            categories.productName=categoryName;
                            categories.quantity=1;
                            categories.taxAmount="0.0";
                            categories.taxPercent="5";
                            categories.description="";
                            categories.productimage=productImage;
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



    public void showdialog()
    {





        final String subtotals;
        // custom dialog
       final  Dialog dialog = new Dialog(ProductDashboard.this, R.style.ThemeDialogCustom);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cartpage);
        dialog.getWindow().setGravity(Gravity.CENTER);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
        //dialog.getWindow().setLayout((8 * width) / 10, (8 * height) / 10);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        String rupee = getResources().getString(R.string.Rupee_symbol);

         cartview=(RecyclerView)dialog.findViewById(R.id.cartlist) ;
        totalprice=(TextView) dialog.findViewById(R.id.total_price) ;
        subtotal=(TextView) dialog.findViewById(R.id.sub_total) ;


        ImageView payment=(ImageView)dialog.findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //wifi();

                Checkout(dialog);


            }
        });
        final int columns = getResources().getInteger(R.integer.grid_column);

        CartAdapter adapter=new CartAdapter(ProductDashboard.this,dialog);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductDashboard.this);
        cartview.setLayoutManager(layoutManager);
        cartview.setItemAnimator(new DefaultItemAnimator());
        cartview.setNestedScrollingEnabled(false);
        cartview.setAdapter(adapter);

        subtotal.setText(rupee+" "+String.format("%.2f",totalvalue()));
        subtotals=String.format("%.2f",totalvalue());


        ImageView iv_close=(ImageView)dialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        // gst_amount = ( adapter.totalvalue() * 18 ) / 100;

        double total=totalvalue()+totalTaxAmount();
        double roundofftotal=Math.round(total);
        totalprice.setText(String.format("%.2f",roundofftotal));

    }





    public double totalvalue()
    {
        double totalValue=0.0;
        for(int i=0;i<global.cartList.size();i++)
        {
            String price=global.cartList.get(i).getPrice();

            double value= Double.parseDouble(price) * global.cartList.get(i).getQuantity();
            totalValue=totalValue + value;

        }

        return totalValue;
    }

    public double totalTaxAmount()
    {
        double totalValue=0.0;
        for(int i=0;i<global.cartList.size();i++)
        {
            String price=global.cartList.get(i).getTaxAmount();

            double value= Double.parseDouble(price);
            totalValue=totalValue + value;

        }

        return totalValue;
    }



    public void Checkout(final Dialog orderdialog) {
        class CheckOutService extends AsyncTask<String, String, String> {
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


                    String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "cart/checkout";
                    WSUtils utils = new WSUtils();

                    JSONObject object;

                    double totaltaxamount=totalTaxAmount();
                    double orderamount=totalvalue();
                    double totalamount=orderamount+totaltaxamount;
                    JSONArray cartList=new JSONArray();
                    for(int i=0;i<global.cartList.size();i++)
                    {
                        Product product=global.cartList.get(i);
                        try {
                            object=new JSONObject();
                            object.put("type",product.categoryUid);
                            object.put("productUid",product.productUid);
                            object.put("quantity",product.quantity);
                            object.put("unitPrice",Double.parseDouble(product.price));
                            object.put("taxPercent",Double.parseDouble(product.taxPercent));
                            object.put("taxAmt",Double.parseDouble(product.taxAmount));
                            object.put("totalAmt",Double.parseDouble(product.totalprice));

                            cartList.put(object);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray arraydetails=new JSONArray();

                    JSONObject result=new JSONObject();
                    result.put("user_id","1");
                    result.put("order_items",cartList);
                    result.put("totalTaxAmt",totaltaxamount);
                    result.put("orderAmt",orderamount);
                    result.put("totalCartAmt",Math.round(totalamount));
                    result.put("order_date",getDateTime());


                    arraydetails.put(result);



                    //usbPrinter();




                    response = utils.responsedetailsfromserver(requestURL, arraydetails.toString());

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


                    if (o == null ) {

                    }else {
                        orderdialog.dismiss();
                        try {
                            JSONObject object=new JSONObject(o);
                            JSONArray result=object.getJSONArray("payload");
                            JSONObject object1=result.getJSONObject(0);
                            String orderStatus=object1.getString("orderStatus");
                            String orderUId=object1.getString("orderUId");

                            if(orderStatus.equalsIgnoreCase("COMPLETED") ) {

                            }
                        } catch (JSONException e) {

                        }


                    }


            }
        }
        new CheckOutService().execute();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:aa");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
