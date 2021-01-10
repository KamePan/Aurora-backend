package cn.edu.aurora.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Document(collection = "Aurora.Image")
public class Image implements Serializable {

    @Id
    private String id;

    private String name;

    private Binary rawpic;

    private static final long serialVersionUID = 1L;

}