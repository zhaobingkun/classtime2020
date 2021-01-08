package com.bigdata.orders;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class OrderPartition extends Partitioner<OrderBean, Text> {
    @Override
    public int getPartition(OrderBean orderBean, Text text, int i) {
        System.out.println("partition"+text);
        return (orderBean.getOrderId().hashCode() & Integer.MAX_VALUE) % i;
    }
}
