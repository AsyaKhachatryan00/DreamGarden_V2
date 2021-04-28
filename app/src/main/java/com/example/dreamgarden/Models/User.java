package com.example.dreamgarden.Models;

public class User {

    public String FName, PNumber, E_mail;

    public User() { }

    public User(String E_mail, String FName, String PNumber) {
        this.E_mail = E_mail;
        this.FName = FName;
        this.PNumber = PNumber;

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
