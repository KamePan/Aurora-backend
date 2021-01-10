package cn.edu.aurora.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class Aurora implements Serializable {

    private String name;

    private String time;

    private String band;

    private String manualtype;

    private String feature;

    private Binary rawpic;

    private Binary thumb;

    private List<Integer> keogram;

    private static final long serialVersionUID = 1L;

    public Aurora(Feature feature, Image image, Keogram keogram, Meta meta, Thumb thumb) {

    }
}
