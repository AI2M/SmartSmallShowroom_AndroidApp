package com.example.tongchaitonsau.smartsmallshowroom;


/**
 * Created by tongchaitonsau on 9/21/2017 AD.
 */

public class Product {
    private int id ;
    private String name;
    private String price;
    private String detail;

    public Product(int id, String name, String detail, String price){
        this.id = id;
        this.name = name;
        this.price  = price;
        this.detail = detail;
    }
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
