/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import wrp.kafkaconsumer.utils.UtilsGeneral;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com, at 11/04/2018
 */
public class MyConsumer {

    /**
     * Main with multi consumers
     *
     * @param args [0]-> route path to the properties file
     * @throws InterruptedException if inter thread operations exceptions occured
     * @throws FileNotFoundException if the path to the config file fails
     * @throws IOException
     */
    public static void main(final String[] args) throws InterruptedException, FileNotFoundException, IOException {
        Properties props = new Properties();
        //set the props to the object
        setProps(args, props);
        //the number of instances should be provided in the args[1] position
        try {
            int numberOfInstances = Integer.parseInt(args[1]);
            runIntances(numberOfInstances, props);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
            System.out.println("The number of instances was not specified, running one instance...");
            runInstance(props);
        }

    }

    /**
     * Runs KafakaConsumers as much numberOfInstance are requested, every kafkaConsumer will run in its own Thread
     *
     * @param numberOfInstances the number of instances that are going to run
     * @param props properties to set in all the instances of consumers
     */
    public static void runIntances(final int numberOfInstances, final Properties props) {
        for (int i = 0; i < numberOfInstances; i++) {
            KafkaConsumerRunner kafkaRunner = new KafkaConsumerRunner(
                    new KafkaConsumer<>(props),
                    Arrays.asList(props.get("topic").toString()),
                    records -> {
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.printf("thread = " + Thread.currentThread().getName() + " offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                        }
                    });
            new Thread(kafkaRunner, "T" + (i + 1)).start();
        }

    }

    /**
     * Run a single instance with the properties especified
     *
     * @param props properties to set in the consumer instance
     */
    public static void runInstance(final Properties props) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(props.get("topic").toString()));
        //run indefinitely
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }

    /**
     * sets the properties to the properties object if exist a properties file specified
     *
     * @param args with the running program, args[0] shold be de properties file path
     * @param properties properties object to put properties
     */
    private static void setProps(final String[] args, final Properties props) throws FileNotFoundException, IOException {
        if (args.length > 0) {
            String rutaArchivo = args[0];
            props.load(new FileInputStream(rutaArchivo));
            System.out.println("Consumer(s) will run with config file: " + rutaArchivo);
        } else {
            props.put("bootstrap.servers", "localhost:9092");
            props.put("topic", "topic.test");
            props.put("group.id", "test");
            props.put("enable.auto.commit", "true");
            props.put("auto.commit.interval.ms", "1000");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            System.out.println("Consumer(s) will run with default configs:");
        }
        UtilsGeneral.printProps(props);
    }

}
