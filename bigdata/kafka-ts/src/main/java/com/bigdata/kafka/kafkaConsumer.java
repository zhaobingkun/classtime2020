package com.bigdata.kafka;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
public class kafkaConsumer {
    public static void main(String[] args) {
        String topicName = "test11";
        String groupId = "testtest";
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop31:9092,hadoop32:9092,hadoop33:9092");
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topicName));
        System.out.println("test22");
        try {
            while (true) {
                //ConsumerRecords<String, String> records =consumer.poll(Integer.MAX_VALUE);
                ConsumerRecords<String, String> records =consumer.poll(1000);
               // ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.offset() + ", " + record.key() + ", " + record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
