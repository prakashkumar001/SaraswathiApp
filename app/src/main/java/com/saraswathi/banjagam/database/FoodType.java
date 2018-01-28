package com.saraswathi.banjagam.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prakash on 1/28/2018.
 */

@Entity
public class FoodType {
    @Id
    Long id;
    String foodType;
    @Generated(hash = 1874371088)
    public FoodType(Long id, String foodType) {
        this.id = id;
        this.foodType = foodType;
    }
    @Generated(hash = 2140850097)
    public FoodType() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFoodType() {
        return this.foodType;
    }
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
