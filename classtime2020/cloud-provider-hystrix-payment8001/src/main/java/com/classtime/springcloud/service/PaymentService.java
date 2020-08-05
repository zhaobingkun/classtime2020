package com.classtime.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    public String paymentInfo_ok(Integer id){
        return "线程 ： " + Thread.currentThread().getName()+" paymentInfo_OK,id: "+  id +"\t" + "测试";
    }
    //3秒内算正常，超过3秒，超时，调用Hy的容错方法
    //降级
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="5000")
    })
    public String paymentInfo_TimeOut(Integer id){
        int timeNumber = 3;
        //int age = 10/0;    //程序出错也会进入容错程序
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程 ： " + Thread.currentThread().getName()+" paymentInfo_TimeOut,id: "+  id +"\t" + "  耗时"+timeNumber+"秒钟";
    }

    public String paymentInfo_TimeOutHandler(Integer id){
        return "线程 ： " + Thread.currentThread().getName()+" paymentInfo_OK,id: "+  id +"\t" + "出问题了。";
    }
    //====熔断
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if(id<0){
            throw new RuntimeException("********id 不能为负");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t" + "调用成功，流水号：" + serialNumber;
    }
    @HystrixCommand(
            fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),// 时间窗口期/时间范围
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")// 失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能为负=======id: " + id;
    }

}
