package com.classtime.springcloud.controller;

import com.classtime.springcloud.entities.CommonResult;
import com.classtime.springcloud.entities.Payment;
import com.classtime.springcloud.service.PaymentService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import jdk.nashorn.internal.runtime.logging.DebugLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Resource
    private DiscoveryClient discoveryClient;


    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);

        System.out.println("*****插入结果:"+result);
        CommonResult commonResult = new CommonResult();
        if(result>0){
            commonResult.setCode(200);
            commonResult.setMessage("插入成功,serverPort:"+serverPort);
            commonResult.setData(result);
            return commonResult;
            //return new CommonResult(200,"插入数据成功",result);
        }else{
            commonResult.setCode(400);
            commonResult.setMessage("插入数据失败");
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
            commonResult.setMessage("查询成功 serverPort"+serverPort);
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

    @GetMapping("/payment/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("*****element:" + element);
        }

        // 一个微服务下的全部实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.debug(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort()
                    + instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }

    @GetMapping(value = "/payment/timeout")
    public String paymentTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

}
