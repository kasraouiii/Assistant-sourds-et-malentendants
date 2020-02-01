package com.example.first.myfirstapplication.Models;

public class AmisModel {
    private int id;
    private String name;
    private byte[] image;
    public AmisModel(int id,String name,byte[] image){
        this.id=id;
        this.name=name;
        this.image=image;
    }
    public AmisModel(int id,String name){
        this.id=id;
        this.name=name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }


}
