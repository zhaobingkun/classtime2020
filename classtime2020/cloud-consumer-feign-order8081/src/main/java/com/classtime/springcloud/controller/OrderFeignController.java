package com.classtime.springcloud.controller;

import com.classtime.springcloud.entities.CommonResult;
import com.classtime.springcloud.entities.Payment;
import com.classtime.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderFeignController {
    @Resource
    private PaymentFeignService paymentFeignService;
    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        log.info(id+"");
        return paymentFeignService.getPaymentById(id);
    }
    @GetMapping(value = "/consumer/payment/timeout")
    public String paymentTimeout(){
        return paymentFeignService.paymentTimeout();
    }
}
