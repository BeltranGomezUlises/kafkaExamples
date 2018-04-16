/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaproducer;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com, at 11/04/2018
 */
public class MyProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 1000; i++) {
            producer.send(new ProducerRecord<>("topic.test", Integer.toString(i), Integer.toString(i)));
        }
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        Thread t1 = new Thread(() -> {
//            while (true) {
//                for (int i = 0; i < 20; i++) {
//                    producer.send(new ProducerRecord<>("my-topic", Integer.toString(i) + "-" + Thread.currentThread().getName(), Integer.toString(i)));
//                }
//            }
//        }, "t1");
//        Thread t2 = new Thread(() -> {
//            while (true) {
//                for (int i = 0; i < 20; i++) {
//                    producer.send(new ProducerRecord<>("my-topic", Integer.toString(i) + "-" + Thread.currentThread().getName(), Integer.toString(i)));
//                }
//            }
//        }, "t2");
//
//        executor.execute(t1);
//        executor.execute(t2);
//
//        executor.shutdown();

        producer.close();
    }
}
