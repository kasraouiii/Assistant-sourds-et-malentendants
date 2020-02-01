package com.example.first.myfirstapplication.Models;

/**
 * Created by reale on 2/28/2017.
 */


public class ChatModel {
    public String message;
    public boolean isSend;
    public String nom;
    public byte [] image;

    public ChatModel(String message, boolean isSend,String nom,byte [] image) {
        this.message = message;
        this.isSend = isSend;
        this.nom=nom;
        this.image=image;
    }

    public ChatModel(String message,boolean isSend) {
        this.isSend=isSend;
        this.message=message;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public byte[] getImage(){
        return image;
    }
    public void setImage(byte[] image){
        this.image=image;
    }

    public String getNom(){
        return nom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
