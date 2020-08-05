package com.classtime.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//注解在类上, 为类提供读写属性, 此外还提供了 equals()、hashCode()、toString() 方法
@Data

//会生成一个包含所有变量，同时如果变量使用了NotNull annotation ， 会进行是否为空的校验，
//全部参数的构造函数的自动生成，该注解的作用域也是只有在实体类上，参数的顺序与属性定义的顺序一致。
@AllArgsConstructor

//无参构造函数
@NoArgsConstructor
public class Payment implements Serializable {
    private  Long id;
    private  String serial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
