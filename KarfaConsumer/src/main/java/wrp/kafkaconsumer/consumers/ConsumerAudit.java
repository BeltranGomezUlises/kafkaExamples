/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import wrp.kafkaconsumer.Main;
import wrp.kafkaconsumer.models.ModelAudit;
import wrp.kafkaconsumer.utils.UtilsDB;
import wrp.kafkaconsumer.utils.UtilsMail;

/**
 *
 * @author Alonso --- alongo@kriblet.com
 */
public class ConsumerAudit implements Consumer<ConsumerRecords<String, String>> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Properties props;

    public ConsumerAudit(Properties props) {
        this.props = props;
    }

    @Override
    public void accept(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {
                ModelAudit ma = mapper.readValue(Base64.getDecoder().decode(record.value()), ModelAudit.class);
                switch (ma.getTipo()) {
                    case HTML_MAIL:
                        UtilsMail.sendHtmlMail(props, ma.getMensaje(), ma.getDestino());
                        break;
                    case MAIL_PLAIN:
                        UtilsMail.sendMail(props, ma.getMensaje(), ma.getDestino());
                        break;
                    case SMS:

                        break;
                    default:
                        throw new AssertionError();
                }
                UtilsDB.insertAudit(ma);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmailException ex) {
                Logger.getLogger(ConsumerAudit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
