package cn.edu.aurora.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bson.types.Binary;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class AuroraVO implements Serializable {

    private String name;

    private String time;

    private String band;

    private String manualtype;

    private String feature;

    private String rawpic;

    private String thumb;

    private static final long serialVersionUID = 1L;
}
