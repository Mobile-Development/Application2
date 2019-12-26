package com.example.myapplication.model;

public class PersonInfo {

    private static PersonInfo instance = new PersonInfo();
    private int Id;
    private int height;
    private int weight;
    private int blood;
    private int sitUpNumber;
    private int pushUpNumber;
    private int gender;
    private int age;
    private int heartBeat;
    private int pullUp;

    private PersonInfo(){};

    public static PersonInfo getInstance(){
        return instance;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getSitUpNumber() {
        return sitUpNumber;
    }

    public void setSitUpNumber(int sitUpNumber) {
        this.sitUpNumber = sitUpNumber;
    }

    public int getPushUpNumber() {
        return pushUpNumber;
    }

    public void setPushUpNumber(int pushUpNumber) {
        this.pushUpNumber = pushUpNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
    }

    public int getPullUp() {
        return pullUp;
    }

    public void setPullUp(int pullUp) {
        this.pullUp = pullUp;
    }
}
