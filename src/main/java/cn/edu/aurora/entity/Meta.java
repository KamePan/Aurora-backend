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
@Document(collection = "Aurora.Meta")
public class Meta implements Serializable {

    @Id
    private String id;

    private String name;

    private String time;

    private String band;

    private String manualtype;

    private static final long serialVersionUID = 1L;

}
