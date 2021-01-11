package cn.edu.aurora.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Document(collection = "Aurora.Feature")
public class Feature implements Serializable {

    @Id
    private String id;

    private String name;

    private String feature;

    private static final long serialVersionUID = 1L;

}
