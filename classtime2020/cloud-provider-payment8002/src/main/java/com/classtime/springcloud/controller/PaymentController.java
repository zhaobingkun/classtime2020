package com.classtime.springcloud.controller;

import com.classtime.springcloud.entities.CommonResult;
import com.classtime.springcloud.entities.Payment;
import com.classtime.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        System.out.println("*****插入结果:"+payment);

        int result = paymentService.create(payment);

        System.out.println("*****插入结果:"+result);
        CommonResult commonResult = new CommonResult();
        if(result>0){
            commonResult.setCode(200);
            commonResult.setMessage("插入成功:serverPort:"+serverPort);
            commonResult.setData(result);
            return commonResult;
            //return new CommonResult(200,"插入数据成功",result);
        }else{
            commonResult.setCode(400);
            commonResult.setMessage("插入失败");
            commonResult.setData(result);
            return commonResult;
            //return new CommonResult(444,"插入数据失败",null);
        }

    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        //log.info("*****插入结果:"+payment);
        System.out.println(payment);
        CommonResult commonResult = new CommonResult();
        if(payment != null){
            commonResult.setCode(200);
            commonResult.setMessage("查询成功：serverPort："+serverPort);
            commonResult.setData(payment);
            return commonResult;
            //return new CommonResult(200,"查询成功",payment);
        }else{
            commonResult.setCode(200);
            commonResult.setMessage("查询失败，查询ID"+id);
            commonResult.setData(null);
            return commonResult;
            //return new CommonResult(444,"查询失败，查询ID"+id,null);
        }

    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }
}
