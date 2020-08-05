package com.classtime.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//让容器扫描的到内容
@Component
public class MyLB implements LoadBalancer {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        return null;
    }
}
