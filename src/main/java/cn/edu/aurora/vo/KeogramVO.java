package cn.edu.aurora.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ApiModel(description = "keogram 查询返回图片数据")
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class KeogramVO  implements Serializable {

    private String keogram;

    private static final long serialVersionUID = 1L;
}
