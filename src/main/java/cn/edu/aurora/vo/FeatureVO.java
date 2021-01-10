package cn.edu.aurora.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ApiModel(description = "相似图片查询返回极光数据")
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class FeatureVO implements Serializable {

    private String name;

    private String time;

    private String band;

    private String manualtype;

    private String thumb;

    @ApiModelProperty("相似度(海明距离)")
    private Integer similarity;

    private static final long serialVersionUID = 1L;
}
