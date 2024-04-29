package com.dugq;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("mongo")
public class CustomMongoConfigurationTest {
    Gson gson = new Gson();

    @Autowired(required = false)
    private MongoClient mongoClient;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @GetMapping("/databasesByClient")
    public String simpleTest(){
        if (mongoClient == null){
            return "No MongoClient found";
        }
        LinkedList<String> list = mongoClient.getDatabase("test").listCollectionNames().into(new LinkedList<>());
        return String.join("\n", list);
    }

    @GetMapping("/addByClient")
    public String addUser(String name, int age,String phone){
        if (mongoClient == null){
            return "No MongoClient found";
        }
        Person person = buildPerson(name, age, phone);
        Document document = Document.parse(gson.toJson(person));
        InsertOneResult result = mongoClient.getDatabase("test").getCollection("first_collection").insertOne(document);
        return  result.getInsertedId().asObjectId().getValue().toHexString();
    }

    private static Person buildPerson(String name, int age, String phone) {
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setGender("男");
        person.setEmail(name +"@example.com");
        person.setPhone(phone);
        return person;
    }

    @GetMapping("listByClient")
    public String listByClient(){
        if (mongoClient == null){
            return "No mongoTemplate found";
        }
        MongoCollection<Document> collection = mongoClient.getDatabase("test").getCollection("first_collection");
        LinkedList<Document> list = collection.find().into(new LinkedList<>());
        String result = list.stream().map(Document::toJson).collect(Collectors.joining(","));
        return "["+result+"]";
    }


    @GetMapping("listByTemplate")
    public String listByTemplate(){
        if (mongoTemplate == null){
            return "No mongoTemplate found";
        }
        // 使用 springBoot 包装方法
        //内部使用反射实现
        List<Person> list = mongoTemplate.findAll(Person.class);
        return gson.toJson(list);
    }

    /**
     * 类似的添加也可以使用execute方法实现<br/>
     * <code>
     *      return mongoTemplate.execute("first_collection",collection -> {
     *             Person person = buildPerson(name, age, phone);
     *             Document document = Document.parse(gson.toJson(person));
     *             return collection.insertOne(document).getInsertedId().asObjectId().getValue().toHexString();
     *         });
     * </code>
     */
    @GetMapping("listByTemplate2")
    public String listByTemplate2(){
        if (mongoTemplate == null){
            return "No mongoTemplate found";
        }
        // 使用execute进入原生模式
        return mongoTemplate.execute("first_collection",collection -> {  LinkedList<Document> list = collection.find().into(new LinkedList<>());
            String result = list.stream().map(Document::toJson).collect(Collectors.joining(","));
            return "["+result+"]";});
    }

    @GetMapping("/addByTemplate")
    public String addUser2(String name, int age,String phone){
        if (mongoTemplate == null){
            return "No mongoTemplate found";
        }
        return mongoTemplate.save(buildPerson(name, age, phone)).getId();
    }

}
