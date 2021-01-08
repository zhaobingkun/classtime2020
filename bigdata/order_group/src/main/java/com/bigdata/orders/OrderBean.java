package com.bigdata.orders;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OrderBean implements WritableComparable<OrderBean> {
    private String OrderId;
    private String Goods;
    private Double Price;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getGoods() {
        return Goods;
    }

    public void setGoods(String goods) {
        Goods = goods;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "OrderId='" + OrderId + '\'' +
                ", Goods='" + Goods + '\'' +
                ", Price=" + Price +
                '}';
    }

    @Override
    public int compareTo(OrderBean o) {
        int i = this.OrderId.compareTo(o.getOrderId());
        if(i==0){
            i = this.Price.compareTo(o.getPrice())*-1;
        }
        return i;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(OrderId);
        dataOutput.writeUTF(Goods);
        dataOutput.writeDouble(Price);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
            this.Goods = dataInput.readUTF();
            this.Goods = dataInput.readUTF();
            this.Price = dataInput.readDouble();
    }
}
