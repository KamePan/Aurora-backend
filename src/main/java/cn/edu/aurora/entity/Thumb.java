package cn.edu.aurora.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@Document(collection = "Aurora.Thumb")
public class Thumb implements Serializable {

    @Id
    private String id;

    private String name;

    private Binary thumb;

    private static final long serialVersionUID = 1L;
}
