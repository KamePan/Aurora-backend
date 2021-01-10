package cn.edu.aurora.vo;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class AuroraVO implements Serializable {

    private String name;

    private String time;

    private String band;

    private String manualtype;

    private String feature;

    private String rawpic;

    private String thumb;

    private String keogram;

    private static final long serialVersionUID = 1L;
}
