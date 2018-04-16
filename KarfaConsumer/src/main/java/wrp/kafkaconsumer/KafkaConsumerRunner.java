/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com, at 11/04/2018
 */
public class KafkaConsumerRunner implements Runnable {

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaConsumer consumer;
    private final List<String> topics;
    private final Consumer function;
    
    public KafkaConsumerRunner(KafkaConsumer consumer, List<String> topics, Consumer<ConsumerRecords<String, String>> function) {
        this.consumer = consumer;
        this.topics = topics;
        this.function = function;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                function.accept(records);
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) {
                throw e;
            }
        } finally {
            consumer.close();
        }
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
    
    
}
