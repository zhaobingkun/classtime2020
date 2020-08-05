package com.classtime.springcloud.controller;

import com.classtime.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderController {

    @Resource
    PaymentHystrixService paymentHystrixService;


    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_ok(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_ok(id);
        return result;
    }
    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
   /* @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="1500")
    })*/

    @HystrixCommand
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id){

        int age = 10/0;    //程序出错也会进入容错程序
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        return result;    }

    public String paymentTimeOutFallbackMethod(Integer id){
        return "线程 ：出问题了,稍后稍后。";
    }

    public String payment_Global_FallbackMethod(){
        return "Global异常处理信息，请稍后再试";
    }



}
