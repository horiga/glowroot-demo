package org.horiga.study.glowroot.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class MessageEntity implements Serializable {

    private static final long serialVersionUID = -6886577044309897447L;

    @Id @GeneratedValue
    Long id;

    @Column(length = 30, nullable = false)
    String project;

    @Column(length = 30, nullable = false)
    String node;

    @Column(length = 10, nullable = false)
    String type;

    @Column(length = 10, nullable = false)
    String priority;

    @Column(nullable = false)
    String body;
}
