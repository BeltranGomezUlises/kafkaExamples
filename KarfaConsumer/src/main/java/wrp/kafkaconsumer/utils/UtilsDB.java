/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.Properties;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.mongojack.JacksonCodecRegistry;
import wrp.kafkaconsumer.models.ModelBitacora;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com, at 12/03/2018
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
    private static MongoCollection<ModelBitacora> collBitacora;

    /**
     * Initialize the database client pool and codecs configs
     *
     * @param props properties object with the connection string and database name
     */
    public static void initDatabase(Properties props) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoClient = new MongoClient(props.getProperty("mongodb.connection"), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        DB = mongoClient.getDatabase(props.getProperty("mongodb.database"));

        JacksonCodecRegistry jacksonCodecRegistry = new JacksonCodecRegistry();
        jacksonCodecRegistry.addCodecForClass(ModelBitacora.class);

        MongoCollection<?> coll = DB.getCollection("bitacora");
        collBitacora = coll.withDocumentClass(ModelBitacora.class).withCodecRegistry(jacksonCodecRegistry);
    }

    /**
     * Saves in the mongo database the object
     *
     * @param bitacora object to save in database
     */
    public static void insert(ModelBitacora bitacora) {
        try {
            collBitacora.insertOne(bitacora);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves in the mongo database all the object
     *
     * @param bitacoras objects to save in database
     */
    public static void insert(List<ModelBitacora> bitacoras) {
        try {
            collBitacora.insertMany(bitacoras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
