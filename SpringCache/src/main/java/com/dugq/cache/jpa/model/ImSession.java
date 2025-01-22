package com.dugq.cache.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "im_session")
@Getter
@Setter
public class ImSession {

    @Id
    @Column(length = 128, nullable = false)
    private String id;

    @Column(length = 32)
    private String appid;

    @Column(length = 32)
    private String uid;

    @Column(length = 32, name = "to_uid")
    private String toUid;

    @Column
    private Byte type;

    @Column(length = 128, name = "session_id")
    private String sessionId;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "commit_seq")
    private Long commitSeq;

    @Column(name = "is_deleted", columnDefinition = "tinyint default 0")
    private Boolean isDeleted;

    @Column(name = "delete_web", columnDefinition = "tinyint default 0")
    private Boolean deleteWeb;

    @Column(name = "delete_app", columnDefinition = "tinyint default 0")
    private Boolean deleteApp;

    @Column(name = "delete_pc", columnDefinition = "tinyint default 0")
    private Boolean deletePc;
}
