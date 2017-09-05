package org.bird.breeze.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

public class MongoDBUtilsTest {

    @Test
    public void testMongoDBUtils(){

        String collName = "person";
        String dbName = "test";
        MongoCollection<Document> coll = MongoDBUtils.instance.getCollection(dbName, collName);
//
//        Document doc = new Document();
//        doc.put("name", "Pompey");
//        doc.put("age", 45);
//        MongoDBUtils.instance.insert(coll, doc);

        Bson filter = Filters.eq("name","Pompey");
        MongoCursor<Document> coll1 = MongoDBUtils.instance.find(coll,filter);
        System.out.println(coll1.next().toJson());
    }
}
