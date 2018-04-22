/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.Properties;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.mongojack.JacksonCodecRegistry;
import wrp.kafkaconsumer.models.ModelAudit;
import wrp.kafkaconsumer.models.ModelJournal;

/**
 *
 * @author Alonso --- alongo@kriblet.com
 */
public class UtilsDB {

    //<editor-fold defaultstate="collapsed" desc="DESCRIPTION">
    /**
     * The MongoClient instance actually represents a pool of connections to the database; you will only need one instance of class MongoClient even with multiple threads.
     *
     */
//</editor-fold>        
    private static MongoClientURI mongoClientUri;
    private static MongoClient mongoClient;
    private static MongoDatabase DB;
    private static MongoCollection<ModelJournal> collectionJournal;
    private static MongoCollection<ModelAudit> collectionAudit;

    /**
     * Initialize the database client pool and codecs configs
     *
     * @param props properties object with the connection string and database name
     */
    public static void initDatabase(Properties props) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoCredential cre = MongoCredential.createCredential(
                props.getProperty("mongodb.user"),
                props.getProperty("mongodb.loginDatabase"),
                props.getProperty("mongodb.pass").toCharArray());

        mongoClient = new MongoClient(
                new ServerAddress(
                        props.getProperty("mongodb.host"),
                        Integer.valueOf(props.getProperty("mongodb.port"))),
                cre,
                MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());

        DB = mongoClient.getDatabase(props.getProperty("mongodb.database"));

        JacksonCodecRegistry jacksonCodecRegistry = new JacksonCodecRegistry();
        jacksonCodecRegistry.addCodecForClass(ModelJournal.class);
        jacksonCodecRegistry.addCodecForClass(ModelAudit.class);

        MongoCollection<?> collJournal = DB.getCollection("journal");
        collectionJournal = collJournal.withDocumentClass(ModelJournal.class).withCodecRegistry(jacksonCodecRegistry);

        MongoCollection<?> collAudit = DB.getCollection("audit");
        collectionAudit = collAudit.withDocumentClass(ModelAudit.class).withCodecRegistry(jacksonCodecRegistry);
    }

    /**
     * Saves in the mongo database the object
     *
     * @param journal object to save in database
     */
    public static void insertJournal(ModelJournal journal) {
        try {
            collectionJournal.insertOne(journal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves in the mongo database all the object
     *
     * @param journals objects to save in database
     */
    public static void insertJournals(List<ModelJournal> journals) {
        try {
            collectionJournal.insertMany(journals);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves in the mongo database the object
     *
     * @param audit object to sabe in database
     */
    public static void insertAudit(ModelAudit audit) {
        try {
            collectionAudit.insertOne(audit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves in the mongo database the object
     *
     * @param audits object to sabe in database
     */
    public static void insertAudits(List<ModelAudit> audits) {
        try {
            collectionAudit.insertMany(audits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
