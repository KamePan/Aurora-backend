package cn.edu.aurora.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Document(collection = "Aurora.Keogram")
public class Keogram implements Serializable {

    @Id
    private String id;

    private String name;

    private List<Integer> keogram;

    private static final long serialVersionUID = 1L;
}
