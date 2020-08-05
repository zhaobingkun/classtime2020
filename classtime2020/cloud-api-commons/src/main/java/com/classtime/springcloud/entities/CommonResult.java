package com.classtime.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*


@AllArgsConstructor
@NoArgsConstructor
*/
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommonResult<T> implements Serializable {  //泛型：如果装的payment 返回payment,装的order 返回order
    private Integer code;
    private String message;
    private T data;
/*
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }*/
    public  CommonResult(Integer code,String message){
        this(code,message,null);
    }

/*    public CommonResult(Integer code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = null;
    }*/
}