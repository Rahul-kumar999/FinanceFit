package com.example.financefit;

public class UserDataClass {
    private String Telephone;
    private String Address;
    private String Postal;
    private String Budget;

    ;



    public UserDataClass(String address, String postal, String telephone, String budget) {

        Telephone = telephone;
        Address = address;
        Postal = postal;
        Budget = budget;

    }


    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPostal() {
        return Postal;
    }

    public void setPostal(String postal) {
        Postal = postal;
    }
    public String getBudget() {
        return Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }



}
