package com.bigdata.orders;



        import org.apache.hadoop.io.WritableComparable;
        import org.apache.hadoop.io.WritableComparator;


public class OrderGroup extends WritableComparator {
    public  OrderGroup(){
        super(OrderBean.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean firstBean = (OrderBean) a;
        OrderBean secondBean = (OrderBean) b;

        System.out.println("group="+firstBean.getOrderId());
        return firstBean.getOrderId().compareTo(secondBean.getOrderId());
    }
}
