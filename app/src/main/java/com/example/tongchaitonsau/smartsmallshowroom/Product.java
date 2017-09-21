package com.example.tongchaitonsau.smartsmallshowroom;


/**
 * Created by tongchaitonsau on 9/21/2017 AD.
 */

public class Product {
    private int imageId ;
    private String name;
    private String price;


    public Product(int imageId,String name,String price){
        this.imageId = imageId;
        this.name = name;
        this.price  = price;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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
