package com.classtime.springcloud.service.impl;

import com.classtime.springcloud.dao.PaymentDao;
import com.classtime.springcloud.entities.Payment;
import com.classtime.springcloud.service.PaymentService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;
    public int create(Payment payment){
        System.out.println(payment.toString());
        return paymentDao.create(payment);
    }
    public Payment getPaymentById(Long id){
        return paymentDao.getPaymentById(id);
    }
}
