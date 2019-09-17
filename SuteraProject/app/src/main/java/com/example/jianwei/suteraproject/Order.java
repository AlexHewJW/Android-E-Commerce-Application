package com.example.jianwei.suteraproject;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * Getter and setters for order forms
 */
public class Order {
    String username,itemname,address;

    public Order(){


    }
    public Order(String username,String itemname, String address){
        this.itemname = itemname;           //ORDER WILL RECORD EMAIL, ITEMNAME AND ADDRESS OF THE BUYER
        this.username = username;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public String getItemname() {
        return itemname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
