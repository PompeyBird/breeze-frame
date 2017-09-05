package org.bird.breeze.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.InputStream;
import java.util.Properties;

public enum MongoDBUtils {

    instance;

    private MongoClient mongoClient;
    static {
        try {
            InputStream is = MongoDBUtils.class.getClassLoader().getResourceAsStream("mongodb-config.properties") ;
            Properties properties = new Properties();
            if(is!=null) {
                properties.load(is);
            }
            String ip = (String)properties.get("mongo.ip");
            Integer port = Integer.parseInt((String)properties.get("mongo.port"));
            instance.mongoClient = new MongoClient(ip, port);
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
