package com.saraswathi.banjagam.common;

import android.app.Application;
import android.content.Context;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import com.saraswathi.banjagam.database.DaoMaster;
import com.saraswathi.banjagam.database.DaoSession;
import com.saraswathi.banjagam.database.Product;
import com.saraswathi.banjagam.helper.Helper;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/19/2017.
 */

public class GlobalClass extends Application{

    public static String UserId="";
    public static String UserName="";
    public static String deFaultBaseUrl="";//http://192.168.0.114
    public static ArrayList<Product> cartList=new ArrayList<>();
    public static String BadgeCount="0";
    public static String bluetoothStatus=null;
    public static String ApiBaseUrl="/prakash/foodcourt/index.php/mobile/";    //192.168.1.110
    public static String ApiImageUrl="/prakash/foodcourt/index.php/mobile/";

    Database db;
    public DaoSession daoSession;
    public void onCreate() {



        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "saraswathydb");

        // db = helper.getWritableDb();
        db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        new Helper(daoSession, this);

        initImageLoader(getApplicationContext());

    }
    public static void initImageLoader(Context context) {



        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)

                .threadPriority(Thread.NORM_PRIORITY - 2)

                .denyCacheImageMultipleSizesInMemory()

                .discCacheFileNameGenerator(new Md5FileNameGenerator())

                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .build();



        ImageLoader.getInstance().init(config);

    }



}
