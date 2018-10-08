package com.thai.domain.user;

import com.thai.infra.jpa.customConfig.JsonStringType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.ZonedDateTime;

@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
})

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "org_uuid")
    public String orgUuid;

    @Column(name = "name")
    public String name;

    @Column(name = "config")
    @Type(type = "json")
    public UserConfig userConfig;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public EntityStatus status;

    @Column(name = "created_time")
    @CreationTimestamp
    public ZonedDateTime createdTime;


}
