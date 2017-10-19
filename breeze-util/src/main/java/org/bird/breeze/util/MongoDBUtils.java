package org.bird.breeze.util;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * The enum Mongo db utils.
 * @author pompey
 */
public enum MongoDBUtils {

    /**
     * Instance mongo db utils.
     */
    instance;

    private MongoClient mongoClient;
    static {
        try {
            InputStream is = MongoDBUtils.class.getClassLoader().getResourceAsStream("mongodb-config.properties") ;
            Properties properties = new Properties();
            if(is!=null) {
                properties.load(is);
            }
            String db = (String)properties.get("mongo.db");
            String user = (String)properties.get("mongo.user");
            String pwd = (String)properties.get("mongo.pwd");
            String ip = (String)properties.get("mongo.ip");
            Integer port = Integer.parseInt((String)properties.get("mongo.port"));
            Integer connectionsPerHost = Integer.parseInt(
                    (String)properties.get("mongo.options.connectionsPerHost"));
            Integer threadsAllowed = Integer.parseInt(
                    (String)properties.get("mongo.options.threadsAllowedToBlockForConnectionMultiplier"));
            Integer maxWaitTime = Integer.parseInt(
                    (String)properties.get("mongo.options.maxWaitTime"));
            Integer connectTimeout = Integer.parseInt(
                    (String)properties.get("mongo.options.connectTimeout"));
            ServerAddress serverAddress=new ServerAddress(ip,port);
            MongoClientOptions options = new MongoClientOptions.Builder()
                            .connectionsPerHost(connectionsPerHost)
                            .threadsAllowedToBlockForConnectionMultiplier(threadsAllowed)
                            .maxWaitTime(maxWaitTime*1000)
                            .connectTimeout(1000*connectTimeout).build();
            List<MongoCredential> lstCredentials =
                    Arrays.asList(MongoCredential.createCredential(
                            user, db, pwd.toCharArray()));
            instance.mongoClient = new MongoClient(serverAddress, lstCredentials, options);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取DB实例 - 指定DB
     *
     * @param dbName
     * @return
     */
    public MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }
    /**
     * 获取collection对象 - 指定Collection
     *
     * @param collName
     * @return
     */
    public MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

    /**条件查询*/
    public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        if(null!=filter){
            return coll.find(filter).iterator();
        }else{
            return coll.find().iterator();
        }
    }
    /**插入一条数据*/
    public void insert(MongoCollection<Document> coll,Document doc){
        coll.insertOne(doc);
    }

    /**更新一条数据*/
    public void update(MongoCollection<Document> coll,Document querydoc,Document updatedoc){
        coll.updateMany(querydoc, updatedoc);
    }

    /**删除一条数据*/
    public void delete(MongoCollection<Document> coll,Document doc){
        coll.deleteMany(doc);
    }
}
