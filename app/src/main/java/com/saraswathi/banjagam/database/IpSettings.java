package com.saraswathi.banjagam.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Creative IT Works on 10-Jan-18.
 */

@Entity
public class IpSettings {
    @Id
    Long id;
    String baseIpAdress="";
    @Generated(hash = 1526114457)
    public IpSettings(Long id, String baseIpAdress) {
        this.id = id;
        this.baseIpAdress = baseIpAdress;
    }
    @Generated(hash = 3440445)
    public IpSettings() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBaseIpAdress() {
        return this.baseIpAdress;
    }
    public void setBaseIpAdress(String baseIpAdress) {
        this.baseIpAdress = baseIpAdress;
    }

}
