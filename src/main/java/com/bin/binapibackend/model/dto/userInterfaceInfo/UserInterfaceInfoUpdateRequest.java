package com.bin.binapibackend.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName interface_info
 */
@TableName(value = "interface_info")
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {
    /**
     * 接口id
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalnum;

    /**
     * 剩余调用次数
     */
    private Integer leftnum;

    /**
     * 0-正常； 1-禁用
     */
    private Integer status;

    public static final long serialVersionUID = 1L;


}