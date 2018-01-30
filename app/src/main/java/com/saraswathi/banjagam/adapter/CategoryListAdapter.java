package com.saraswathi.banjagam.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.saraswathi.banjagam.R;
import com.saraswathi.banjagam.activities.ProductDashboard;
import com.saraswathi.banjagam.common.GlobalClass;
import com.saraswathi.banjagam.database.Categories;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakash on 1/27/2018.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {


    List<Categories> categories = new ArrayList<>();

    GlobalClass global;
    Context context;
    ImageLoader loader;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  productname;
        public ImageView product_image;




        public MyViewHolder(View view) {
            super(view);

            product_image = (ImageView) view.findViewById(R.id.product_image);
            productname = (TextView) view.findViewById(R.id.category_name);


        }
    }


    public CategoryListAdapter(Context context, List<Categories> categories) {

        global=new GlobalClass();
        this.context=context;
        this.categories=categories;
        loader=ImageLoader.getInstance();

    }

    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_item, parent, false);


        return new CategoryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryListAdapter.MyViewHolder holder, final int position) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.app_icon) // resource or drawable
                .showImageForEmptyUri(R.drawable.app_icon) // resource or drawable
                .showImageOnFail(R.drawable.app_icon) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(100)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();


        holder.productname.setText(categories.get(position).getCategoryName());
          try
        {
            loader.displayImage(global.deFaultBaseUrl+global.ApiImageUrl+categories.get(position).getCategoryName(),holder.product_image,options);

        }catch (Exception e)
        {

        }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             Intent i=new Intent(context,ProductDashboard.class);
               i.putExtra("sub_id",categories.get(position).categoryUid);
                context.startActivity(i);
           }
       });







    }

    @Override
    public int getItemCount() {
        return categories.size();
    }





    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
