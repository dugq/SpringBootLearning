package com.dugq;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("mongo")
public class CustomMongoConfigurationTest {
    Gson gson = new Gson();

    @Autowired(required = false)
    private MongoClient mongoClient;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoPageHelper mongoPageHelper;

    private  Random random = new Random();

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
        Person person = buildPerson(name, age, phone,random.nextInt());
        Document document = Document.parse(gson.toJson(person));
        InsertOneResult result = mongoClient.getDatabase("test").getCollection("first_collection").insertOne(document);
        return  result.getInsertedId().asObjectId().getValue().toHexString();
    }

    private Person buildPerson(String name, int age, String phone,long id) {
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setUserId(id);
        person.setGender("男");
        person.setEmail(name +"@example.com");
        person.setPhone(phone);
        person.setLocation(new GeoJsonPoint(random.nextFloat(180),random.nextFloat(90)));
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

    @GetMapping("geo/near")
    public PageResult<?> geoNear(Long userId,int pageIndex,int pageSize){
        Person userNearPo = findOne(userId);
        if(userNearPo == null){
            throw new IllegalArgumentException("用户未开启定位功能");
        }

        // Create query criteria with geo location
        Criteria geoCriteria = Criteria.where("location")
                .nearSphere(userNearPo.getLocation())
                .maxDistance(1000L);

        // Create query with criteria and sort by distance
        Query query = new Query(geoCriteria)
                .addCriteria(Criteria.where("gender").is("男"));
        PageResult<Person> personPageResult = mongoPageHelper.pageQuery(query, Person.class, pageIndex, pageSize);
        List<Person> list = personPageResult.getList();
        for (Person person : list){
            double distance = calculateDistance(userNearPo.getLocation().getX(), userNearPo.getLocation().getY(), person.getLocation().getX(), person.getLocation().getY());
            person.setDistance(distance);
        }
        return personPageResult;
    }

    private static final int EARTH_RADIUS = 6371; // 地球半径，单位千米

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double lat1 = Math.toRadians(y1);
        double lon1 = Math.toRadians(x1);
        double lat2 = Math.toRadians(y2);
        double lon2 = Math.toRadians(x2);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c; // 距离，单位千米

        return distance;
    }

    @GetMapping("findOne")
    public Person findOne(Long userId){
        return mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), Person.class);
    }


    @GetMapping("generationData")
    public String generationData(int start){
        for(int i = 0; i < 100000;i++){
            long id = start+i;
            Person person = new Person();
            person.setName("random:"+id%100);
            person.setAge((int)id%100);
            person.setUserId(id);
            person.setGender("男");
            person.setEmail(person.getName() +"@example.com");
            person.setPhone("188"+id%100000L);
            person.setLocation(new GeoJsonPoint(random.nextFloat(180),random.nextFloat(90)));
            mongoTemplate.save(person);
        }
        return "success";
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

        return mongoTemplate.save(buildPerson(name, age, phone,random.nextInt())).getId();
    }

}
