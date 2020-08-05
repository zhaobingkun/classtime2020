package com.classtime.springcloud.entities;

public class Sington {
    private static  volatile Sington instance = null;

    private Sington(){}

    public Sington getInstance(){
        if(instance == null){
            synchronized (Sington.class){
                if (instance == null){
                    instance = new Sington();
                }
            }

        }
        return instance;
    }

}
