package com.saraswathi.banjagam.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Prakash on 10/25/2017.
 */

@Entity
public class Categories {
    @Id
    public Long categoryId;
    public String categoryName;
    public String categoryImage;
    public String categoryUid;
    public String categoryTypeId;
    public String active;
    @Generated(hash = 427374877)
    public Categories(Long categoryId, String categoryName, String categoryImage,
            String categoryUid, String categoryTypeId, String active) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryUid = categoryUid;
        this.categoryTypeId = categoryTypeId;
        this.active = active;
    }
    @Generated(hash = 267348489)
    public Categories() {
    }
    public Long getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getCategoryImage() {
        return this.categoryImage;
    }
    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
    public String getCategoryUid() {
        return this.categoryUid;
    }
    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid;
    }
    public String getActive() {
        return this.active;
    }
    public void setActive(String active) {
        this.active = active;
    }
    public String getCategoryTypeId() {
        return this.categoryTypeId;
    }
    public void setCategoryTypeId(String categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

}
