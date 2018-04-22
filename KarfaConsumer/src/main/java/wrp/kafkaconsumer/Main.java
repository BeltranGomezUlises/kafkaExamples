/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer;

import wrp.kafkaconsumer.consumers.KafkaConsumerRunner;
import wrp.kafkaconsumer.consumers.ConsumerAudit;
import wrp.kafkaconsumer.consumers.ConsumerJournal;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import wrp.kafkaconsumer.utils.UtilsDB;
import wrp.kafkaconsumer.utils.UtilsGeneral;

/**
 *
 * @author Alonso --- alongo@kriblet.com
 */
public class Main {

    /**
     * Main with multi consumers
     *
     * @param args [0]-> route path to the properties file, [1] -> number of instances of consumers to journal, [2] -> number of instances of consumers to audit
     * @throws InterruptedException if inter thread operations exceptions occured
     * @throws FileNotFoundException if the path to the config file fails
     * @throws IOException
     */
    public static void main(final String[] args) throws InterruptedException, FileNotFoundException, IOException {
        Properties props = new Properties();
        //set the props to the object
        setProps(args, props);
        UtilsDB.initDatabase(props);
        //the number of instances should be provided in the args[1] position
        try {
            int numberOfInstancesJournal = Integer.parseInt(args[1]);
            runIntancesJournal(numberOfInstancesJournal, props);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("The number of instances to journal was not specified, running one instance...");
            runInstanceJournal(props);
        }

        try {
            int numberOfIntancesAudit = Integer.parseInt(args[2]);
            runIntancesAudit(numberOfIntancesAudit, props);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("The number of instances to audit was not specified, running one instance...");
            runIntanceAudit(props);
        }
    }

    /**
     * Runs KafakaConsumers as much numberOfInstance are requested, every kafkaConsumer will run in its own Thread
     *
     * @param numberOfInstancesJournal the number of instances that are going to run
     * @param props properties to set in all the instances of consumers
     */
    public static void runIntancesJournal(final int numberOfInstancesJournal, final Properties props) {
        for (int i = 0; i < numberOfInstancesJournal; i++) {
            KafkaConsumerRunner kafkaRunner = new KafkaConsumerRunner(
                    new KafkaConsumer<>(props),
                    Arrays.asList(props.get("topic.journal").toString()),
                    new ConsumerJournal());
            new Thread(kafkaRunner, "T-Journal" + (i + 1)).start();
        }
    }

    /**
     * Run a single instance with the properties especified
     *
     * @param props properties to set in the consumer instance
     */
    public static void runInstanceJournal(final Properties props) {
        ObjectMapper mapper = new ObjectMapper();
        KafkaConsumerRunner kafkaRunner = new KafkaConsumerRunner(
                new KafkaConsumer<>(props),
                Arrays.asList(props.get("topic.journal").toString()),
                new ConsumerJournal());
        new Thread(kafkaRunner, "T-Journal").start();
    }

    /**
     * Run KafkaConsumers to audit as much numberOfInstancesAudit is indicated
     *
     * @param numberOfIntancesAudit the number of instances to run
     * @param props properties to set in the consumers instances
     */
    private static void runIntancesAudit(int numberOfIntancesAudit, Properties props) {
        for (int i = 0; i < numberOfIntancesAudit; i++) {
            KafkaConsumerRunner kafkaRunner = new KafkaConsumerRunner(
                    new KafkaConsumer<>(props),
                    Arrays.asList(props.get("topic.audit").toString()),
                    new ConsumerAudit(props));
            new Thread(kafkaRunner, "T-Audit" + (i + 1)).start();
        }
    }

    /**
     * Run a single KafkaConsumer to audit
     *
     * @param props properties to set in the consumers instances
     */
    private static void runIntanceAudit(Properties props) {
        KafkaConsumerRunner kafkaRunner = new KafkaConsumerRunner(
                new KafkaConsumer<>(props),
                Arrays.asList(props.get("topic.audit").toString()),
                new ConsumerAudit(props));
        new Thread(kafkaRunner, "T-Audit").start();
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
            props.put("bootstrap.servers", "192.168.1.70:9092");
            props.put("topic.journal", "wrp-journal");
            props.put("topic.audit", "wrp-audit");
            props.put("group.id", "auth");
            props.put("enable.auto.commit", "true");
            props.put("auto.commit.interval.ms", "1000");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            //db
            props.put("mongodb.user", "administrador");
            props.put("mongodb.pass", "mongo.90Y9B8yh$123");
            props.put("mongodb.database", "auth");
            props.put("mongodb.loginDatabase", "admin");
            props.put("mongodb.host", "201.165.0.142");
            props.put("mongodb.port", "27170");

            //mail
            props.put("mail.host", "smtp.googlemail.com");
            props.put("mail.port", "465");
            props.put("mail.userMail", "usuariosexpertos@gmail.com");
            props.put("mail.userPass", "90Y9B8yh$123");
            props.put("mail.userNameFrom", "mail owner");
            props.put("mail.subject", "auditoria");
            System.out.println("Consumer(s) will run with default configs:");
        }
        UtilsGeneral.printProps(props);
    }
}
