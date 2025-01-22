package com.dugq.cache.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "im_user", indexes = {@Index(name = "idx_account", columnList = "account", unique = true)
})
@Getter
@Setter
public class ImUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 6297457456628780184L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String account;

    @Column(name = "nick_name", length = 255)
    private String nickName;

    private Integer age;

    @Column(length = 255)
    private String avatar;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}