package com.classtime.springcloud.entities;

public class Sing {
    private  static volatile  Sing instance = null;
    private Sing(){}
    public Sing getInstance(){
        if(instance==null){
            synchronized (Sing.class){
                if(instance==null){
                    instance = new Sing();
                }
            }
        }

        return instance;
    }
}
