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
public class KeogramVO  implements Serializable {

    private String keogram;

    private static final long serialVersionUID = 1L;
}
