package com.dugq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
// 使用springBoot封装API时，内部借助了声明式配置。此处使用@Document注解将Person类映射到MongoDB的first_collection集合中。如果不写，默认使用类名作为集合名。
@Document(collection = "first_collection")
public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 3666691069758351826L;

    private String id;
    private Long userId;
    private String name;
    private int age;
    private String gender;
    private String email;
    private String phone;
    @GeoSpatialIndexed(name = "location")
    @JsonProperty("location")
    private Point location;
    private double distance;

}
