package cn.edu.aurora.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ApiModel("通过名称返回图片数据")
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class ImageVO implements Serializable {

    private String image;

    private static final long serialVersionUID = 1L;
}