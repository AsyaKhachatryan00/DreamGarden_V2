package com.example.dreamgarden.Models;

public class User {

    private String FName, PNumber, E_mail, Address, Uid;

    public User() { }

    public User(String FName, String PNumber, String e_mail, String address, String uid) {
        this.FName = FName;
        this.PNumber = PNumber;
        E_mail = e_mail;
        Address = address;
        Uid = uid;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getPNumber() {
        return PNumber;
    }

    public void setPNumber(String PNumber) {
        this.PNumber = PNumber;
    }

    public String getE_mail() {
        return E_mail;
    }

    public void setE_mail(String e_mail) {
        E_mail = e_mail;
    }

}
