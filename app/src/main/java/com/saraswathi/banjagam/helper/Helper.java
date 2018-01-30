package com.saraswathi.banjagam.helper;

import android.content.Context;


import com.saraswathi.banjagam.database.Categories;
import com.saraswathi.banjagam.database.DaoSession;
import com.saraswathi.banjagam.database.FoodType;
import com.saraswathi.banjagam.database.IpSettings;
import com.saraswathi.banjagam.database.Product;
import com.saraswathi.banjagam.database.ProductDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Creative IT Works on 10-Aug-17.
 */

public class Helper {
    private final DaoSession daoSession;

    Context context;


    public Helper(DaoSession daoSession, Context context) {
        this.daoSession = daoSession;
        this.context = context;
        helper = this;
    }

    public static Helper getHelper() {
        return helper;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Helper helper;



    //getMenuItems
    public List<Categories> getCategoryItems() {

        QueryBuilder<Categories> qb = daoSession.queryBuilder(Categories.class);
        return qb.list();

    }

    //getMenuItems
    public List<FoodType> getFoodType() {

        QueryBuilder<FoodType> qb = daoSession.queryBuilder(FoodType.class);
        return qb.list();

    }


    //getMenuItems
    public List<Product> getProductItems(String subcategoryId) {

        QueryBuilder<Product> qb = daoSession.queryBuilder(Product.class);
        qb.where(ProductDao.Properties.CategoryUid.eq(subcategoryId));

        return qb.list();

    }

    public IpSettings getIpAddress() {

        QueryBuilder<IpSettings> qb = daoSession.queryBuilder(IpSettings.class);
        qb.limit(1);
        return  qb.unique();


    }
}
