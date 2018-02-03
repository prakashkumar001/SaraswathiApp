package com.saraswathi.banjagam.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.adapter.CartAdapter;
import com.saraswathi.banjagam.adapter.CategoryListAdapter;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.database.Categories;

import com.saraswathi.banjagam.database.FoodType;
import com.saraswathi.banjagam.database.Product;
import com.saraswathi.banjagam.utils.WSUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public static TextView totalprice,subtotal;
    public  static  RecyclerView cartview;


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



        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (global.cartList.size() > 0) {
            cartcount.setText(String.valueOf(global.cartList.size()));
        }

        cartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showdialog();



            }
        });




        getCategoryList("1");

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

                List<Categories> categoriesList=new ArrayList<>();

                if (o == null ) {
                    adapter=new CategoryListAdapter(DashBoard.this,categoriesList);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                    productListView.setLayoutManager(layoutManager);
                    productListView.setItemAnimator(new DefaultItemAnimator());
                    productListView.setNestedScrollingEnabled(false);
                    productListView.setAdapter(adapter);

                }else {


                    try {

                        JSONArray array=new JSONArray(o);


                        if(array.length()==0)
                        {
                            adapter=new CategoryListAdapter(DashBoard.this,categoriesList);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                            productListView.setLayoutManager(layoutManager);
                            productListView.setItemAnimator(new DefaultItemAnimator());
                            productListView.setNestedScrollingEnabled(false);
                            productListView.setAdapter(adapter);

                        }else {
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject data=array.getJSONObject(i);
                                String categoryId=data.getString("id");
                                String categoryName=data.getString("sub_categoryname");
                                String categoryTypeId=data.getString("main_catid");
                                String categoryImage=data.getString("books_url");

                                String active=data.getString("status");

                                Categories categories=new Categories();
                                categories.categoryId=(Long.parseLong(categoryId));
                                categories.categoryUid=categoryId;
                                categories.categoryTypeId=categoryTypeId;
                                categories.categoryImage=categoryImage;
                                categories.categoryName=categoryName;
                                categories.active=active;

                                getHelper().getDaoSession().insertOrReplace(categories);

                            }

                            categoriesList=getHelper().getCategoryItems();

                            adapter=new CategoryListAdapter(DashBoard.this,categoriesList);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                            productListView.setLayoutManager(layoutManager);
                            productListView.setItemAnimator(new DefaultItemAnimator());
                            productListView.setNestedScrollingEnabled(false);
                            productListView.setAdapter(adapter);



                        }



                    } catch (JSONException e) {
                        e.printStackTrace();




                    }
                }









            }
        }
        new CategoryServer().execute();
    }

    public void showdialog()
    {





        final String subtotals;
        // custom dialog
        final Dialog dialog = new Dialog(DashBoard.this, R.style.ThemeDialogCustom);


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

        CartAdapter adapter=new CartAdapter(DashBoard.this,dialog);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashBoard.this);
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
                dialog = new ProgressDialog(DashBoard.this);
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


