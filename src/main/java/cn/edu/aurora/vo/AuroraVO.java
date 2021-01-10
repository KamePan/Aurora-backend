package cn.edu.aurora.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;


@ApiModel("参数查询返回极光数据")
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class AuroraVO implements Serializable {

    @ApiModelProperty("图片名称(主键)")
    private String name;

    @ApiModelProperty("记录时间")
    private String time;

    @ApiModelProperty("冠冕")
    private String band;

    @ApiModelProperty("类型")
    private String manualtype;

    @ApiModelProperty("Base64 编码图片")
    private String rawpic;

    @ApiModelProperty("Base64 编码缩略图")
    private String thumb;

    private static final long serialVersionUID = 1L;
}
