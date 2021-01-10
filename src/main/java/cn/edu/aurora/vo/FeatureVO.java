package cn.edu.aurora.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class FeatureVO implements Serializable {

    private String name;

    private String time;

    private String band;

    private String manualtype;

    private String feature;

    private String rawpic;

    private String thumb;

    private Integer similarity;

    private static final long serialVersionUID = 1L;
}
