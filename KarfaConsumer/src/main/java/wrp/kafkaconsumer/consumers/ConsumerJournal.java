/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import wrp.kafkaconsumer.Main;
import wrp.kafkaconsumer.models.ModelJournal;
import wrp.kafkaconsumer.utils.UtilsDB;

/**
 *
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
public class ConsumerJournal implements Consumer<ConsumerRecords<String, String>> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void accept(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {
                ModelJournal mb = mapper.readValue(Base64.getDecoder().decode(record.value()), ModelJournal.class);
                UtilsDB.insertJournal(mb);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.printf("thread = " + Thread.currentThread().getName() + " offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            //System.out.printf("thread = " + Thread.currentThread().getName() + " offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

}
