package com.example.myapplication.Greendao.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserInfo {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "NAME")
    private String name;

    @Property(nameInDb = "PASSWORD")
    private String passWord;

    @Property(nameInDb = "HEIGHT")
    private float height = 0;

    @Property(nameInDb = "WEIGHT")
    private float weight = 0;

    public UserInfo(Long id, String name, String passWord) {
        this.id = id;
        this.name = name;
        this.passWord = passWord;
    }

    @Generated(hash = 1198695335)
    public UserInfo(Long id, String name, String passWord, float height,
            float weight) {
        this.id = id;
        this.name = name;
        this.passWord = passWord;
        this.height = height;
        this.weight = weight;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public void setHeight(float height){this.height = height;}
    public void setWeight(float weight) {this.weight = weight;}
    public void setName(String name) {this.name = name;}
    public void setPassWord(String passWord) {this.passWord = passWord;}

    public Long getId() {return this.id;}
    public String getName() {return this.name;}
    public String getPassWord() {return this.passWord;}
    public Float getHeight() {return this.height;}
    public Float getWeight() {return this.weight;}

    public void setId(Long id) {
        this.id = id;
    }
}
