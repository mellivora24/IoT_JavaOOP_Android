package com.javaoop.smarthome;

public class Users {
    private String uid;
    private String name;
    private String email;
    private String phonenumber;

    public Users(){
        this.uid = uid;
        this.phonenumber = phonenumber;
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName(){
        return  name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
