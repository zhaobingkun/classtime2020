package com.classtime.springcloud.controller;

//import lombok.Value;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private  String serverPort;

    @RequestMapping(value = "/payment/consul")
    public String paymentConsul(){
        return "springcloud : " + serverPort +"\t" + UUID.randomUUID().toString();
    }


}
