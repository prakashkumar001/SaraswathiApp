package com.saraswathi.banjagam.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

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

        adapter=new ProductListAdapter(ProductDashboard.this,new ArrayList<Product>());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        productListView.setLayoutManager(layoutManager);
        productListView.setItemAnimator(new DefaultItemAnimator());
        productListView.setNestedScrollingEnabled(false);
        productListView.setAdapter(adapter);


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
}
